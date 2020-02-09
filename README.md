# Spacetaxi

_It's dangerous to go alone! Take this_

This piece of technology will help you navigate between the explored systems using the established space highways.

## How to run?

You will need Maven3 and Java13 installed on your space cruiser (the only allowed technologies decided by the council).

Execute `mvn clean install` inside this directory. As soon as the build finishes you should be able to run it with the `taxi` command. If you are not able to execute `taxi` you are probably running an unauthorized operation system on your space cruiser. In that case you could try `java -jar target\spacetaxi-0.0.1-SNAPSHOT-jar-with-dependencies.jar` and hope that the council will not detect your disobedience.

Being successful you should see the help and further usage information displayed.

### Features

* You can lookup if your desired route is valid and how long it will take via the `taxi route` command. Example:
  * `taxi route "Solar System -> Sirius"`

* You can search for valid routes between two systems. There are multiple command to find the desired routes
  * Search by shortest travel time: `taxi shortest "Alpha Centauri" Betelgeuse`
  * Search by exact amounts of stops: `taxi exact --value 4 Sirius Sirius`
  * Search by maximum stops or travel time: `taxi max --type=time --value 29 Sirius Sirius` 

As already mentioned you can use the help system to see a more options and explanation `taxi --help`.

### Assumptions
The current interstellar highway structure only supports travel time in full hours, as a experience space traveller should know.