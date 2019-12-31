# Zendesk Search App
This repo is to submit my code exercise to Zendesk.
Zendesk describes that it is fine for me to use Public repo for my exercise.


# Assumption
1. For me this is malformed ISO8601 format, `"2016-05-21T11:10:28 -10:00"` but assumed that it is on purpose.
  - There should not be `space` before timezone adjustment, `-10:00`

2. As description says I assumed that I don't need to concern about RAM resource much.

3. I assumed that I am allowed to use library for better search performance so will use the given data structure like map.


# Approach
I did my best to produce production-like code.

## Functional Programming
In the job description, FP is desired skill so thought that I could apply my best knowledge of FP for my code exercise.
- Admittedly this could decrease some developers' comprehension to my code.
- But in REA, my team did our best to help new developer's learning journey for Scala so should be fine I reckon.

## Search performance




## Libraries
- [Circe]()
- [Cats]()
- [Cats-Effect]()
- [Spec2]()
- [Scala Check]() for Simple Property Check

# Dev Env

## Setup Environment
**sometime after running your test from docker the file owner tend to be changed as `root`**

This command will fix the issue

> sudo chown -r $USER:$USER .
>
### Without Docker
1. [Install Open JDK 8](https://openjdk.java.net/install/)
2. [Install sbt](https://www.scala-sbt.org/)
2. [Scala 2.13.1](https://www.scala-lang.org/)
4. Execute `Commands` below for either test or execute app

#### Commands

##### Run Test

`sbt clean test`

##### Run App

`sbt clean assembly`
`scala target/scala-2.13/zendesk.jar`

### With Docker
1. [Install Latest Docker](https://docs.docker.com/v17.12/install/)
2. [Install Docker Compose](https://docs.docker.com/compose/install/)
3. Use docker container, `auto/dev-environment`
4. Execute `Commands** below for either test or execute app

#### Commands

##### Get into Docker Container
`auto/dev-environment`

##### Run Test
`auto/test`

##### Run App
`auto/run`
