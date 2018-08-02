---
title: Java Password based symmetric file encryption using JDK
keywords: sample
summary: "Password based symmetric file encryption in Java"
permalink: java_file_encryption_symmetric_password_based.html
folder: Java JDK
references: [
    # Place a list of references used to create and/or understand this example.
    {
        url: "https://docs.oracle.com/javase/10/docs/api/javax/crypto/Cipher.html",
        description: "Java JDK Ciper"
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
last_updated: "2018-08-02"
tags: [Java, AES, GCM, PBKDF2, Salt, AEAD]
---

## Use cases

- Password based symmetric encryption of a file

## Java version

- JDK 11

## Example Code for Java Password based symmetric file encryption using AES-GCM and PBKDF2

```java
{% include_relative src/main/java/com/cryptoexamples/java/ExampleFileEncryption.java %}
```



{% include links.html %}
