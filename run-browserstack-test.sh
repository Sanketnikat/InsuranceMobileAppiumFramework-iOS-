#!/bin/sh
set -eu

test_selector=${1:-}

if [ -z "$test_selector" ] || [ "${test_selector#*#}" = "$test_selector" ]; then
    echo "Usage: ./run-browserstack-test.sh fully.qualified.TestClass#testMethod" >&2
    exit 2
fi

test_class=${test_selector%%#*}
test_method=${test_selector#*#}
suite_file="target/browserstack-single-test-matrix.xml"

mkdir -p target
cat > "$suite_file" <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="BrowserStack Single Test Matrix" parallel="tests" thread-count="4">
    <test name="iOS - iPhone 13">
        <parameter name="platform" value="ios"/>
        <parameter name="deviceName" value="iPhone 13"/>
        <parameter name="platformVersion" value="18"/>
        <parameter name="app" value="${BROWSERSTACK_IOS_APP:-bs://90565391cf7d734c3b9017acf02ec70c452b22d2}"/>
        <classes><class name="$test_class"><methods><include name="$test_method"/></methods></class></classes>
    </test>
    <test name="iOS - iPhone 17 Pro">
        <parameter name="platform" value="ios"/>
        <parameter name="deviceName" value="iPhone 17 Pro"/>
        <parameter name="platformVersion" value="26"/>
        <parameter name="app" value="${BROWSERSTACK_IOS_APP:-bs://90565391cf7d734c3b9017acf02ec70c452b22d2}"/>
        <classes><class name="$test_class"><methods><include name="$test_method"/></methods></class></classes>
    </test>
    <test name="Android - Samsung Galaxy M32">
        <parameter name="platform" value="android"/>
        <parameter name="deviceName" value="Samsung Galaxy M32"/>
        <parameter name="platformVersion" value="11.0"/>
        <parameter name="app" value="${BROWSERSTACK_ANDROID_APP:-bs://bb48ac2ba94a530eb75306bbcee9f8a8c3dda0f7}"/>
        <classes><class name="$test_class"><methods><include name="$test_method"/></methods></class></classes>
    </test>
    <test name="Android - Samsung Galaxy S24 Ultra">
        <parameter name="platform" value="android"/>
        <parameter name="deviceName" value="Samsung Galaxy S24 Ultra"/>
        <parameter name="platformVersion" value="14.0"/>
        <parameter name="app" value="${BROWSERSTACK_ANDROID_APP:-bs://bb48ac2ba94a530eb75306bbcee9f8a8c3dda0f7}"/>
        <classes><class name="$test_class"><methods><include name="$test_method"/></methods></class></classes>
    </test>
</suite>
EOF

mvn test -Dtestng.suiteXmlFile="$suite_file"