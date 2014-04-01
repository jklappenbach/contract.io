#!/bin/bash
pushd .
cd ..
java -jar ./target/contract.io.app-1.0.jar --plugin-path ../contract.io.java/target --plugin java-1.0 --input-path ./test --protocol tcp:2012 --encoding contract --output-path ../contract-test/src/main/java/
popd

