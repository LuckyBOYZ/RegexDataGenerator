# RegexDataGenerator

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=plastic)](https://opensource.org/licenses/Apache-2.0)
[![codecov](https://codecov.io/gh/LuckyBOYZ/RegexDataGenerator/branch/main/graph/badge.svg?token=WYMHGAQIQG)](https://codecov.io/gh/LuckyBOYZ/RegexDataGenerator)

# Table of contents

[Description](https://github.com/LuckyBOYZ/RegexDataGenerator#description) <br>
[Requirements](https://github.com/LuckyBOYZ/RegexDataGenerator#requirements) <br>
[How To Use](https://github.com/LuckyBOYZ/RegexDataGenerator#how-to-use) <br>
[Special Input Data](https://github.com/LuckyBOYZ/RegexDataGenerator#special-input-data) <br>
[How To Run](https://github.com/LuckyBOYZ/RegexDataGenerator#how-to-run) <br>
[TODO](https://github.com/LuckyBOYZ/RegexDataGenerator#todo) <br>

# Description

Application let you save a time on creating dummy data for example when you want to use `json-server`
to create API for your front-end application. Really big thanks for owner [RgxGen](https://github.com/curious-odd-man/RgxGen) application.
In short: RegexDataGenerator is using RgxGen to generate json file based on regular expresion or by special property value to generate
random real data (like PESEL or polish names)

# Requirements

To run this application you have to install `java 17` on your machine.

# How To Use

For using `RegexDataGenerator` you have to prepare specific json file and put it in the same
directory where you have an `jar` file. This is an example:

```json
[
  {
    "$iteration$": 7,
    "threeDigits": "\\d{3}",
    "sixLettersLowercase": "[a-z]{6}",
    "arrayWithThreeStrings": [
      "\\w{1,5}",
      3
    ],
    "objectWithTwoProperties": {
      "someUppercaseTwoLetters": "[A-Z]{2}",
      "arrayWithLowercaseThreeLettersAndOneNumber": [
        "[a-z]{3}\\d",
        2
      ]
    },
    "arrayOfTwoObjects": [
      {
        "$iteration$": 2,
        "name": "name[A-C]{3}",
        "surname": "surname[D-F]{2}"
      }
    ]
  }
]

```
Apart of `$iteration$` every properties keys are up to you (this property will be explained later. <br>
The value for those properties can be typical regex or you can use [Special Input Data](https://github.com/LuckyBOYZ/RegexDataGenerator#special-input-data) name. <br> 
Below is an overview with the most important information about 
creating configuration for every data type:

### string
You can use regular expression, some hardcoded value (e.g. `abcd`) or `Special Input Data` name (please check **table of contents**)
### object
The name for every property is up to you apart of `$iteration$`. This property tells application how many objects must be created and it works **ONLY** inside of array. <br>
The name for this property can be configured (please check `How To Run` section)
### array
Array requires 1 element (string or object) but can be used second optional element - how many elements application must create (value must be the number)
String and object must be created in the same way as it is described above.

# Special Input Data
You can use special values to create some random data but without regular expresion. <br>
Here you have an example of file:
```json
[
  {
    "$iteration$": 5,
    "name": "NAME",
    "surname": "SURNAME",
    "pesel": "PESEL",
    "iban": "IBAN",
    "id": "ID",
    "city": "CITY",
    "street": "STREET",
    "postcode": "POSTCODE",
    "voivodeship": "VOIVODESHIP",
    "address": "ADDRESS"
  }
]
```
and this is an example result:
```json
[
  {
    "address": {
      "city": "Radom",
      "street": "Czarna 169",
      "postcode": "26-606",
      "voivodeship": "Mazowieckie",
      "county": "Radom"
    },
    "city": "Racibórz",
    "surname": "Świtała",
    "street": "Barska 70",
    "iban": "48168026071333993994432069",
    "name": "Stefan",
    "postcode": "26-606",
    "voivodeship": "Łódzkie",
    "id": 1,
    "pesel": "94033121676"
  }
]
```
There are 2 ways to use special values:
1. use only name of special value (e.g. `SURNAME`)
2. use name of special value with parameters. If some special input data has parameters, then after special name **MUST** be used pipe -> `|`
Here are all `Special Input Data` values and theirs configuration:

## NAME

Example value with all parameters: `NAME|startAt=a,female=true`

| **PROPERTY NAME** |         **DESCRIPTION**         |          **POSSIBLE VALUE**          |
|:-----------------:|:-------------------------------:|:------------------------------------:|
|    **startAt**    |    First letter of the name     | every letter (a-z), case insensitive |
|    **female**     | Is the name must be for female? |             true, false              |

# How To Run

# TODO

- [ ] Adding `Dockerfile`
- [ ] Adding possibility to use name of `Special Input Data` but like a typical regex
- [ ] Creating UI and API for using app through a browser


- kiedy jest sam pipe podany do argumentu
- kiedy jest parametr ale jest on tylko znakiem =
