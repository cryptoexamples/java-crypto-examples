---
title: Java Password Based String Encryption using JDK
keywords: sample
summary: "Password based string encryption in Java"
permalink: java_string_encryption_password_based_symmetric.html
folder: Java JDK
references: [
    # Place a list of references used to create and/or understand this example.
    {
        url: "https://docs.oracle.com/javase/10/docs/api/javax/crypto/Cipher.html",
        description: "Oracle JDK Cipher Documentation"
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
last_updated: "2020-02-16"
tags: [Java, AES, GCM, PBKDF2, Salt, AEAD]
---

## Use cases

- Password based encryption
- Previously shared common secret (password)

## Java version

- openjdk8
- oraclejdk9
- openjdk9
- oraclejdk11
- openjdk11
- oraclejdk13
- openjdk13

## Example Code for Java Password Based String Encryption using AES-GCM and PBKDF2

```java
{% include_relative src/main/java/com/cryptoexamples/java/ExampleStringEncryptionPasswordBased.java %}
```



{% include links.html %}
