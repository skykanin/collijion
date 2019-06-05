# Collijion &middot; [![CircleCI](https://circleci.com/gh/skykanin/collijion.svg?style=svg)](https://circleci.com/gh/skykanin/collijion) [![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)

An attempt at a concurrent N-body simulator in using JavaFX and STM concurrency constructs.

## Installation

TODO: Finish this section when JAR files are pushed to releases and a docker container is made.

## Setup developer environment

To develop on this project you need to have [Java 11](https://openjdk.java.net/projects/jdk/11/) and [Clojure 1.10](https://www.clojure.org/guides/getting_started#_clojure_installer_and_cli_tools) installed. Lastly you will need to install the latest version of [Leiningen](https://leiningen.org/#install).

After installing all these dependencies you can simply clone the repository using `git clone https://github.com/skykanin/collijion.git`. Thereafter, simply `cd` into the project directory and run `lein run` to start the simulation. To run the unit tests simply run `lein test`. Code formatting is done via `lein cljfmt fix`.
