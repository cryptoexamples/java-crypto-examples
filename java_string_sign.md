---
title: Java String Signing using JDK
keywords: sample
summary: "Java based string signing"
permalink: java_string_sign.html
folder: Java JDK
references: [
    # Place a list of references used to create and/or understand this example.
    {
        url: "https://docs.oracle.com/javase/10/docs/api/java/security/Signature.html",
        description: "Oracle JDK Signature Documentation"
    }
]
authors: [
    {
        name: "Kai Mindermann",
        url: "https://github.com/kmindi"
    }
]
# List all reviewers that reviewed this version of the example. When the example is updated all old reviews
# must be removed from the list below and the code has to be reviewed again. The complete review process
# is documented in the main repository of CryptoExamples
current_reviews: [

]
# Indicates when this example was last updated/created. Reviews don't change this.
last_updated: "2018-05-13"
tags: [Java, hash, SHA, SHA-512]
---

## Use cases

- verifying if a string has been changed

## Sample Code for Java based signing of a String using SHA-512, RSA 4096, BASE64 and UTF-8 encoding

```java
{% include_relative src/main/java/com/cryptoexamples/java/ExampleSignatureInOneMethod.java %}
```



{% include links.html %}
