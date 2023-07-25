﻿# RegexDataGenerator

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

Application let you spend less time on creating dummy data for example when you want to use `json-server`
to create API for your front-end application

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
It must be

# Special Input Data

# How To Run

# TODO

- [ ] Adding `Dockerfile`
- [ ] Adding possibility to use name of `Special Input Data` but like a typical regex
- [ ] Creating UI and API for using app through a browser


- kiedy jest sam pipe podany do argumentu
- kiedy jest parametr ale jest on tylko znakiem =
