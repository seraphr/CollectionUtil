CollectionUtils
=====================

Java用コレクションユーティリティライブラリです。
幾つかの高階関数とか実装してます。

とりあえず絶対必要そうなのは揃った…？
色々実装方法が迷走していて、scalaのコレクション・フレームワークの凄さを感じている所存。

ver.0.2 からscalaのコード（wrapper関係）を別プロジェクトに分離


CI : https://buildhive.cloudbees.com/job/seraphr/job/CollectionUtil/
[![Build Status](https://buildhive.cloudbees.com/job/seraphr/job/CollectionUtil/badge/icon)](https://buildhive.cloudbees.com/job/seraphr/job/CollectionUtil/)

Licensed under the "2-clause BSD license"


Maven
=====

```xml
  <repositories>
		<repository>
			<id>seraph-repo</id>
			<name>seraph-repo</name>
			<url>http://seraphr.github.com/maven/</url>
		</repository>
	</repositories>
  ...
  <dependencies>
		<dependency>
			<groupId>jp.seraphr</groupId>
			<artifactId>collection-util</artifactId>
			<version>0.3</version>
		</dependency>
	</dependencies>
```