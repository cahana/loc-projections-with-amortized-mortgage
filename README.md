# loc-projections-with-amortized-mortgage

This project calculates how long it would take to pay off a HELOC in second position with amortized mortgage schedule as additional input

## Getting Started

This is a java program meant to be executed standalone via the command line or an IDE such as Eclipse or IntelliJ.

### Prerequisites

* Oracle JDK8 or higher
* Microsoft Excel to easily read the output (optional)

## Running the program

The java class is execute is org.finance.service.Projection

Input
* Interest rate(s), use comma delimited list if using more than 1 rate
* Outstanding Balance of the Line of Credit
* Floor amount dictating when to make a lump sum payment to HELOC and mortgage
* Amount of lump sump payment
* Month to start the calculation
* Year to start the calculation
* Location of Expense/Income file
* Location of Income file, if user getting paid every other week; leave file empty if not applicable
* Location of Output file

```
1,2,3.75 75000 3000 50000 11 2016 /projections/input/expenses.txt /projections/input/income.txt /projections/results/results.csv
```

Output is a csv file
* Date
* Description
* Type (Expense or Income)
* Amount of Expense/Income
* Balance on Line of Credit
* Daily Interest Owed
* Interest Paid to Date
* summary
** number of lump sum payments made
** interest and time saved from mortgage during each lump sum period
** interest payed and time spent for HELOC during lump sum period
** net interest and time saved during lump sum period
** original mortgage payoff interest and time
** total mortgage interest saved
** total time it'll take to payoff mortgage

## Versioning

For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

See the list of [contributors](https://github.com/your/project/contributors) who participated in this project.


