SHELL := /bin/bash

# This target loads environment variables from the .env file and ensures they are
# available to child processes like Gradle and Spring Boot.
#
# - `set -a` enables automatic export of all variables defined afterward
# - `source .env` loads variables from the file into the current shell
# - `set +a` disables automatic export to avoid side effects
# - `./gradlew bootRun` starts the application with the exported variables
#
# This approach avoids issues where Gradle cannot see variables defined only in the shell.
run:
	set -a && source .env && set +a && ./gradlew bootRun