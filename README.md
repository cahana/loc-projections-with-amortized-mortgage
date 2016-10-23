# loc-projections

This project calculates how long it would take to pay off a Line of Credit.

## Getting Started

This is a java program meant to be executed standalone via the command line or an IDE such as Eclipse or IntelliJ.

### Prerequisites

* Oracle JDK8 or higher
* Microsoft Excel to easily read the output (optional)

## Running the program

The java class is execute is org.finance.service.Projection

Input
* Interest rate(s), use comma delimited list if using more than 1 rate
* Balance of the Line of Credit
* Month to start the calculation
* Year to start the calculation
* Location of Expense/Income file
* Location of Income file, if user getting paid every other week; leave file empty if not applicable
* Location of Output file

```
1,2,3.75 75000 11 2016 /projections/input/expenses.txt /projections/input/income.txt /projections/results/results.csv
```

Output is a csv file
* Date
* Description
* Type (Expense or Income)
* Amount of Expense/Income
* Balance of Line of Credit
* Accrued Interest (up to that day)
* Running Interest Total

## Versioning

For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

See the list of [contributors](https://github.com/your/project/contributors) who participated in this project.


