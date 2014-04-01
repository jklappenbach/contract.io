package org.cajeta.contract;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.cajeta.contract.idl.ContractModule;
import org.cajeta.contract.plugin.CodeGenerator;
import org.cajeta.contract.plugin.Encoding;
import org.cajeta.contract.plugin.Protocol;
import org.cajeta.contract.type.Type;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * Hello world!
 * 
 */
public class Contract {
	private static final String parserVersion = "1.0";
	public static List<Protocol> protocols;
	public static List<Encoding> encodings;	
	public static String rootPath = "";
	public String help = null;
	public static final Queue<ContractModule> moduleQueue = new ConcurrentLinkedQueue<ContractModule>();
	public static final Map<Integer, Type> typeMap = new ConcurrentHashMap<Integer, Type>();
	public static final Set<String> namespaces = Collections.synchronizedSet(new HashSet<String>());
	//private static Contract theContract;
	private static Reflections reflections;
	public static Map<String, String> parameters;
	private static Contract theInstance = new Contract(); 
	
	private Contract() { }
	
	public static Contract getInstance() { return Contract.theInstance; }
	
	private Map<String, String> convertArguments(String[] args) {
		Map<String, String> parameters = new HashMap<String, String>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--")) {
				if (args[i].equals(Constants.ARG_OUTPUT_PATH)) {
					Contract.rootPath = args[i + 1];
				} else if (args[i].equals(Constants.ARG_PROTOCOL)) {
					Contract.protocols = Protocol.fromString(args[i + 1]);
				} else if (args[i].equals(Constants.ARG_ENCODING)) {
					Contract.encodings = Encoding.fromString(args[i + 1]);
				}
				parameters.put(args[i], args[++i]);
			}
		}
		return parameters;
	}	

	public void process(String[] args) throws Exception {
		process(convertArguments(args));
	}
	
	public void process(Map<String, String> parameters) throws Exception {
		Contract.parameters = parameters;
		
		help = loadResource("usage.txt");
		
		if (parameters.containsKey("--help") || parameters.containsKey("-help") || parameters.size() == 0) {
			System.out.println(help);
			return;
		}
		
		if (parameters.containsKey("-version") || parameters.containsKey("--version") || parameters.containsKey("-v")) {
			System.out.println("Contract IDL System v" + parserVersion + ", vendor: cajeta.org\n");
			return;
		} else if (parameters.containsKey("-help") || parameters.containsKey("--help")) {
			try {
				System.out.println(new DataInputStream(Contract.class.getClassLoader().getResourceAsStream("/usage.txt"))
					.readUTF());
			} catch (IOException e) {
				System.out.println("Error: This binary has been corrupted.  Please update or replace.");
			}
			return;
		}
		
		String value = parameters.get(Constants.ARG_PLUGIN);
		
		if (value != null && value.contains("?")) {
			System.out.println("The following language plugins have been installed:\n");
			List<String> pluginNames = getPluginNames();
			for (String pluginName : pluginNames) {
				System.out.println("\t" + pluginName);
			}
			return;
		}
		
		URLClassLoader pluginLoader;
		try {
			pluginLoader = getPluginLoader();
		} catch (Exception e1) {
			System.out.println("Error:  Could not load the plugin.");
			e1.printStackTrace();
			System.exit(-1);
			return;
		}
		
		ClassLoader appLoader = Contract.class.getClassLoader();
		Set<URL> urls = new HashSet<URL>(ClasspathHelper.forClassLoader(pluginLoader));
		urls.addAll(ClasspathHelper.forClassLoader(appLoader));
		reflections = new Reflections(new ConfigurationBuilder()
		        .setUrls(urls)
		        .addClassLoaders(pluginLoader, appLoader) // pluginLoader)
		        .setScanners(new SubTypesScanner(false)));

		// ApplicationPath search
		CodeGenerator generator = null;
		Set<Class<? extends CodeGenerator>> classes = reflections.getSubTypesOf(CodeGenerator.class);
		if (classes.size() > 0) {
			for (Class<? extends CodeGenerator> c : classes) {
				if (c == null) {
					System.out.println("Error:  Could not load plugin class -- Implementation of TypeGenerator not found.");
					System.exit(-1);
					return;
				}
					
				try {
					generator = (CodeGenerator) c.newInstance();
				} catch (Exception e) {
					System.out.println("Error: An exception occured creating the code generator for " + 
							parameters.get(Constants.ARG_PLUGIN));
					e.printStackTrace();
					System.exit(-1);
					return;
				}
			}
		} else {
			System.out.println("No classes were found implementing ContractCodeGenerator.  Program will exit without action.");
			System.exit(-1);
		}
				
		value = parameters.get(Constants.ARG_PROTOCOL);
		if (value != null && value.contains("?") && parameters.get(Constants.ARG_PLUGIN) != null) {
			// Output protocols supported by plugin
		}

		String path = parameters.containsKey(Constants.ARG_INPUT_PATH) ? 
				parameters.get(Constants.ARG_INPUT_PATH) : ".";
				
		process(new File(path));
				
		// At this point, we have all files processed, and populated the Queue of modules.
		// For now, call into a hard-coded java exporter
		try {
			generator.execute(protocols, encodings.get(0), moduleQueue, rootPath);
		} catch (Exception e) {
			System.out.println("Error: a processing exception was thrown while generating code.");
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		System.out.println("Finished processing.");
	}
	
	public void process(File path) throws Exception {
		if (!path.isDirectory()) {
			// Attempt to process a single file
			Thread thread = new Thread(new ContractParseThread(path));
			thread.start();
			thread.join();
		} else {
			List<Thread> threadList = new LinkedList<Thread>();
			for (File child : path.listFiles(new FileFilter() {
				public boolean accept(File candidate) {
					return candidate.isDirectory() || candidate.getName().endsWith(".xml");
				}})) {
				if (child.isDirectory()) {
					process(child);
				} else {
					Thread thread = new Thread(new ContractParseThread(child));
					thread.start();
					threadList.add(thread);
				}
			}
			
			for (Thread t : threadList) {
				t.join();
			}
		}
	}
	
	public synchronized boolean addNamespace(String namespace) {
		if (namespaces.contains(namespace)) return false;
		namespaces.add(namespace);
		return true;
	}
	
	public Set<String> getNamespaces() { 
		return namespaces; 
	}
	
	public void addType(String name, Type type) throws Exception {
		if (typeMap.containsKey(name.hashCode()))
			throw new Exception("Duplicate Type found, may be due to naming collision, or duplicate defintion.");
		typeMap.put(name.hashCode(), type);
	}

	/**
	 * @return the modules
	 */
	public static Queue<ContractModule> getModules() {
		return moduleQueue;
	}

	private URLClassLoader getPluginLoader() throws Exception {
		String pluginPath = parameters.get(Constants.ARG_PLUGIN_PATH);
		String plugin = parameters.get(Constants.ARG_PLUGIN);
		if (pluginPath == null)
			pluginPath = "./plugins";
		
		if (!pluginPath.endsWith(File.separator))
			pluginPath += File.separator;
		
		pluginPath = pluginPath + "contract.io." + plugin + ".jar";
		
		File filePlugin = new File(pluginPath);
		if (!filePlugin.exists())
			throw new Exception("Plugin file does not exist at: " + pluginPath);
		
		return new URLClassLoader(new URL[] { filePlugin.toURI().toURL() });
	}
	
	private List<String> getPluginNames() {
		String appsPath = parameters.get(Constants.ARG_PLUGIN_PATH);
		if (appsPath == null)
			appsPath = "." + File.separatorChar + "plugin";
		
		File directory = new File(appsPath);
		File[] children = directory.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory()) 
					return false;
				else
					return file.getName().endsWith(".jar"); 
			}			
		});
		
		List<String> result = new LinkedList<String>();
		for (File child : children) {
			result.add(child.getName().substring(0, child.getName().length() - 4));
		}
		return result;
	}	
	
	private String loadResource(String resourceName) {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(resourceName);
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		try {
			String read = br.readLine();
			while (read != null) {
				sb.append(read + "\r\n");
				read = br.readLine();
			} while (read != null);
		} catch (IOException e) {
			System.out.println("Error: Could not load internal file '" + resourceName + "'");
			e.printStackTrace();
		}

		return sb.toString();	
	}

	public static Map<String, String> getParameters() {
		return Contract.parameters;
	}

}
