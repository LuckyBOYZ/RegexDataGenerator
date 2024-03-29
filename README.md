﻿# RegexDataGenerator

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=plastic)](https://opensource.org/licenses/Apache-2.0)
[![codecov](https://codecov.io/gh/LuckyBOYZ/RegexDataGenerator/branch/main/graph/badge.svg)](https://codecov.io/gh/LuckyBOYZ/RegexDataGenerator)

# Table of contents

1. [Description](https://github.com/LuckyBOYZ/RegexDataGenerator#description)
2. [Requirements](https://github.com/LuckyBOYZ/RegexDataGenerator#requirements)
3. [How To Run](https://github.com/LuckyBOYZ/RegexDataGenerator#how-to-run)
    1. [Running by using java](https://github.com/LuckyBOYZ/RegexDataGenerator#running-by-using-java)
    2. [Running by using docker](https://github.com/LuckyBOYZ/RegexDataGenerator#running-by-using-docker)
4. [How To Use](https://github.com/LuckyBOYZ/RegexDataGenerator#how-to-use)
5. [Special Input Data](https://github.com/LuckyBOYZ/RegexDataGenerator#special-input-data)
   1. [NAME](https://github.com/LuckyBOYZ/RegexDataGenerator#name)
   2. [SURNAME](https://github.com/LuckyBOYZ/RegexDataGenerator#surname)
   3. [PESEL](https://github.com/LuckyBOYZ/RegexDataGenerator#pesel)
   4. [IBAN](https://github.com/LuckyBOYZ/RegexDataGenerator#iban)
   5. [ID](https://github.com/LuckyBOYZ/RegexDataGenerator#id)
   6. [POSTCODE](https://github.com/LuckyBOYZ/RegexDataGenerator#postcode)
   7. [STREET](https://github.com/LuckyBOYZ/RegexDataGenerator#street)
   8. [CITY](https://github.com/LuckyBOYZ/RegexDataGenerator#city)
   9. [VOIVODESHIP](https://github.com/LuckyBOYZ/RegexDataGenerator#voivodeship)
   10. [COUNTY](https://github.com/LuckyBOYZ/RegexDataGenerator#county)
   11. [ADDRESS](https://github.com/LuckyBOYZ/RegexDataGenerator#address)
6. [TODO](https://github.com/LuckyBOYZ/RegexDataGenerator#todo)
7. [GOTCHA](https://github.com/LuckyBOYZ/RegexDataGenerator#gotcha)

# Description

This application lets you save a time when creating dummy data for example when you want to use `json-server`
to create API for your front-end application. <br>
In short: RegexDataGenerator generates json file based on another input json configuration file with regular expressions
or with
special properties to generate random real data (such as PESEL or polish names).

> :notebook_with_decorative_cover: **WORTH TO MENTION:** every available syntax for regular expressions you can
> check [here](https://github.com/curious-odd-man/RgxGen#supported-syntax)
> and every limitation is available [here](https://github.com/curious-odd-man/RgxGen#limitations)

# Requirements

To run this application you have to install `java 17` on your machine if you want to use `jar` file directly or
install `docker` if you want to run in that way.

> :notebook_with_decorative_cover: **PLEASE NOTE:** Before using this application first of all you **have to**
> prepare `json` configuration file. It is described in
> section [How To Use](https://github.com/LuckyBOYZ/RegexDataGenerator#how-to-use)

# How To Run

_AVAILABLE ARGUMENTS FOR APPLICATION_

|   **JAR ARGUMENT NAME**   | **DOCKER ARGUMENT ENV NAME** |                                                                        **DESCRIPTION**                                                                        |                   **VALUES**                    | **DEFAULT VALUE** |
|:-------------------------:|:----------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------:|:-----------------:|
|    iterationFieldName     |     ITERATION_FIELD_NAME     |                 name of 'how many objects have to be created in array' key property in json. This prop is **only** used for objects in array.                 |        every valid name for key in json         |     iteration     |
|      iterationNumber      |       ITERATION_NUMBER       |                                                            value for `iterationFieldName` argument                                                            |            every number more than 0             |        10         |
|       jsonFileName        |        JSON_FILE_NAME        | name of input file - it can be path but file **HAS TO** be inside the same folder as application (e.g. dummy/test.jar and file is under dummy/test/file.json) | every valid path to file with `.json` extension |     file.json     |
|      prefixAndSuffix      |      PREFIX_AND_SUFFIX       |      character that is used before and after special keys. At the moment this property is used only for creating `iterationFieldName` as a key in object      |                 every character                 |         $         |
| specialInputDataSeparator | SPECIAL_INPUT_DATA_SEPARATOR |                                               separator between `SpecialInputData` name and properties for this                                               |                 every character                 |        \|         |
|     isFormattedResult     |     IS_FORMATTED_RESULT      |                                               whether the result must be formatted (multiline and indentations)                                               |                `true` or `false`                |      `false`      |

## Running by using java

To use the application by using `jar` file, execute the command `java -jar RegexDataGenerator.jar <ARGUMENTS>`.<br>
The latest version of application is placed into `bin` directory. There are few arguments that you can add to command
to customize how the application must work (but it is not mandatory, if you don't use them then the application will use
default values).
Those arguments are placed in _AVAILABLE ARGUMENTS FOR APPLICATION_ table (please use names from `JAR ARGUMENT NAME`
column).

Those arguments **HAVE TO** be seperated by `=`
e.g. `java -jar RegexDataGenerator.jar jsonFileName=test.json iterationFieldName=it`.
The result will be always in the same directory as the application is run.

> :warning: **WARNING:** If you use `jsonFileName` for `java` command then it will refer to **FILE**, not to **DIRECTORY
**<br>
> but it can refer to deeper directory relative to current path e.g. if you have `jar` file under `/usr/my/path/app.jar`
> path<br>
> then you can place you json file under `/usr/my/path/additional/dir/file.json` path. To recap: `file.json` has to
> be<br>
> in the same directory or in directory that is relative to directory with `jar` file.

> :warning: **WARNING:** Please check whether your json configuration file has the same name as the file name in `jsonFileName`
> property. If those names are different e.g. locally you have `file.json` but in property you have `test.json` then
> the application will throw an error.

## Running by using docker

For using docker you need to create an image by command `docker build -t <YOUR_IMAGE_NAME>`. After that you need to<br>
run it by command<br>
`docker run --name <YOUR_CONTAINER_NAME> -it -v <DIRECTORY_WITH_JSON_FILE>:/app/file <YOUR_IMAGE_NAME>`<br>
You can customize the application in the same way as it mentioned in [Running by using java](https://github.com/LuckyBOYZ/RegexDataGenerator#running-by-using-java) section.<br>
Arguments for docker are placed in _AVAILABLE ARGUMENTS FOR APPLICATION_ table in `DOCKER ARGUMENT ENV NAME` column.<br>

> :notebook_with_decorative_cover: **TIP:** you can use `env.list` file instead of writing a couple of env flags in docker command
> (`docker run ... -e <ARGUMENT_WITH_VALUE> -e <SECPND_ARGUMENT_WITH_VALUE> ...`). To use `env.list` file please use the flag and value in
> docker run command like this `docker run --env-file env.list ...` and change those properties, which you need.

For using those arguments please add `-e` to `docker run...` command e.g.:<br>
`docker run --name <YOUR_CONTAINER_NAME> -it -e ITERATION_FIELD_NAME=it -v <DIRECTORY_WITH_JSON_FILE>:/app/file <YOUR_IMAGE_NAME>`

> :warning: **WARNING:** Please be aware that `<DIRECTORY_WITH_JSON_FILE>` in docker command **ALWAYS** refers to *
*DIRECTORY**, not to the file.
> If you use the path like `/usr/some/direcotry/myJson.json` then the application will throw an error.

# How To Use

The mandatory thing to use `RegexDataGenerator` application is preparing specific `json` configuration file and place it
in the same
directory where you have an `jar` file or where you create `volume` for docker. This is an example of content of json
file:

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

Apart of `$iteration$` key, every property key is up to you (`$iteration$` property will be explained later). <br>
The value for those properties can be a classic regular expression, or you can
use [Special Input Data](https://github.com/LuckyBOYZ/RegexDataGenerator#special-input-data) name. You can use
either `array` and `object` as a root in config file. <br>
Below is an overview with the most important information about creating configuration for every data type:

### string

You can use regular expression, some hardcoded value (e.g. `abcd`)
or [Special Input Data](https://github.com/LuckyBOYZ/RegexDataGenerator#special-input-data) name.

### object

The name for every property is up to you apart of `$iteration$`. This property tells the application how many objects
must
be created, and it works **ONLY** if the object is inside of array.

### array

Array requires 1 element (string or object) but can be used a second optional element - how many elements the
application has to
create. A value for second parameter **HAS TO** be a number. If the second element is not a number the default value for
iteration (10) will be
used.
String and object must be created in the same way as it is described above.

# Special Input Data

You can use special values for creating some a random data but without regular expression. <br>
Here you have an example of file:

```json
[
  {
    "$iteration$": 1,
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

There are 2 ways for using special values:

1. use only name of special value (e.g. `SURNAME`)
2. use name of special value with parameters (if any). If some special input data has parameters, then after special
   name **HAS TO** be used a value for `specialInputDataSeparator` argument (default is a pipe -> `|`). Using all
   parameters is not
   obligated, you can use only those, which you need.

> :warning: **WARNING:** Please be aware that using `specialInputDataSeparator` value without any property after this
> will cause that this value will
> be omitted in the result!

> :warning: **WARNING:** When you're using properties with `Special Input Data` you **HAVE TO** use them as _key_=
_value_
> and every property must be seperated by comma (`,`) from another one like this
> _key1_=_value1_,_key2_=_value2_ <br>
> You can't use empty string as a key or value because property will be omitted in the result.


Here are all `Special Input Data` values and theirs configuration:

### NAME

> :notebook_with_decorative_cover: **Description:** It generates one of the name that is used in Poland. If application
> cannot find any value for parameters then is returned `Jan` for male and `Janina` for female

| **PROPERTY NAME** |         **DESCRIPTION**         |          **POSSIBLE VALUE**          |
|:-----------------:|:-------------------------------:|:------------------------------------:|
|    **startAt**    |    First letter of the name     | every letter (a-z), case insensitive |
|    **female**     | Does a name must be for female? |          `true` or `false`           |

**Example:**

Input file with correct values:

```json
{
  "somePropName": "NAME|startAt=a,female=true"
}
```

**Result:**

```json
{
  "somePropName": "Alicja"
}
```

Input file with incorrect values:

```json
{
  "somePropName": "NAME|startAt=$,female=ABCD"
}
```

**Result:**

```json
{
  "somePropName": "Jan"
}
```

Input startAt with incorrect value but female with `true` value:

```json
{
  "somePropName": "NAME|startAt=$,female=true"
}
```

**Result:**

```json
{
  "somePropName": "Janina"
}
```

### SURNAME

> :notebook_with_decorative_cover: **Description:** It generates one of the surname that is used in Poland. If
> application
> cannot find any value for parameters then is returned `Kowalski` for male and `Kowalska` for female

| **PROPERTY NAME** |          **DESCRIPTION**           |          **POSSIBLE VALUE**          |
|:-----------------:|:----------------------------------:|:------------------------------------:|
|    **startAt**    |    First letter of the surname     | every letter (a-z), case insensitive |
|    **female**     | Does a surname must be for female? |          `true` or `false`           |

**Example:**

Input file with correct values:

```json
{
  "somePropName": "SURNAME|startAt=a,female=true"
}
```

**Result:**

```json
{
  "somePropName": "Adamczak"
}
```

Input file with incorrect values:

```json
{
  "somePropName": "SURNAME|startAt=$,female=ABCD"
}
```

**Result:**

```json
{
  "somePropName": "Kowalski"
}
```

Input startAt with incorrect value but female with `true` value:

```json
{
  "somePropName": "SURNAME|startAt=$,female=true"
}
```

**Result:**

```json
{
  "somePropName": "Kowalska"
}
```

### PESEL

> :notebook_with_decorative_cover: **Description:** It generates PESEL number. PESEL is like an ID number for every
> person in
> Poland.

| **PROPERTY NAME** |                    **DESCRIPTION**                     | **POSSIBLE VALUE** |
|:-----------------:|:------------------------------------------------------:|:------------------:|
| **bornAfter2000** | Does a pesel must be for person whose born after 2000? | `true` or `false`  |
|    **female**     |            Does a pesel must be for female?            | `true` or `false`  |
|  **onlyAdults**   | Does application must to create pesel only for adults? | `true` or `false`  |

**Example:**

Input file with correct values:

```json
{
  "somePropName": "PESEL|bornAfter2000=true,female=true,onlyAdults=true"
}
```

**Result:**

```json
{
  "somePropName": "05322838106"
}
```

Input file with incorrect values:

```json
{
  "somePropName": "PESEL|bornAfter2000=nope,female=truthy,onlyAdults=falsy"
}
```

**Result:**

```json
{
  "somePropName": "13032584417"
}
```

### IBAN

> :notebook_with_decorative_cover: **Description:** It generates valid IBAN number BUT as a default the application
> doesn't generate the letters at the beginning of number and without spaces (please check properties below)

| **PROPERTY NAME** |                             **DESCRIPTION**                              |         **POSSIBLE VALUE**         |
|:-----------------:|:------------------------------------------------------------------------:|:----------------------------------:|
|    **country**    |               Country from table with available countries                |      Available Country Table       |
|   **bankName**    |                   Bank name from table with bank names                   | Bank Name Table for chosen country |
|   **formatted**   | Does iban have to split the number by space between every 4th character? |         `true` or `false`          |
|  **withLetters**  |             Does iban must contain letters at the beginning?             |         `true` or `false`          |

_AVAILABLE COUNTRY TABLE_

| **AVAILABLE COUNTRY** | **VALUE** |
|:---------------------:|:---------:|
|      **Poland**       |    PL     |

_POLAND BANK NAMES TABLE_

|             **BANK NAME**              |  **VALUE**   |
|:--------------------------------------:|:------------:|
|        **Narodowy Bank Polski**        |     NBP      | 
|          **PKO Bank Polski**           |    PKOBP     | 
|         **Citibank Handlowy**          |     CITI     | 
|          **ING Bank Śląski**           |     ING      | 
|    **Santander Consumer Bank S.A.**    | SANTANDER_PL | 
|    **Bank Gospodarstwa Krajowego**     |     BGK      | 
|              **mBank SA**              |    MBANK     | 
|           **Bank Millenium**           |  MILLENNIUM  | 
|          **Bank Pekao S.A.**           |    PKOSA     | 
|          **Bank Pocztowy SA**          |   POCZTOWY   | 
|    **Bank Ochrony Środowiska S.A.**    |     BOS      | 
|         **Mercedes-Benz Bank**         |   MERCEDES   | 
|      **SGB-Bank Spółka Akcyjna**       |     SGB      | 
|           **Plus Bank S.A.**           |     PLUS     | 
|          **Société Générale**          |   SOCIETE    | 
|             **Nest Bank**              |     NEST     | 
| **Bank Polskiej Spółdzielczości S.A.** |     BPS      | 
|        **Bank Credit Agricole**        |   AGRICOLE   | 
|    **BNP Paribas Bank Polska S.A.**    |     BNP      | 
|    **Santander Consumer Bank S.A.**    | SANTANDER_CS | 
|      **Toyota Bank Polska S.A.**       |    TOYOTA    | 
|          **DNB Bank Polska**           |     DNB      | 
|             **Alior Bank**             |    ALIOR     | 
| **Ford Credit Europe Bank Polska SA**  |     FCE      | 
|               **InBank**               |    INBANK    | 
|       **Volkswagen Bank Polska**       |  VOLKSWAGEN  | 
|         **HSBC Holdings plc**          |     HSBC     | 
|         **BFF Banking Group**          |     BFF      | 
|          **Aion Bank NV/SA**           |     AION     | 
|           **VeloBank S.A.**            |     VELO     | 

**Example:**

Input file with correct values:

```json
{
  "somePropName": "IBAN|country=PL,bankName=BFF,formatted=true,withLetters=true"
}
```

**Result:**

```json
{
  "somePropName": "PL78 2850 6487 8839 8059 4744 2334"
}
```

Input file with incorrect values:

```json
{
  "somePropName": "IBAN|country=GB,bankName=UNKNOWN,formatted=abcd,withLetters=falsy"
}
```

**Result:**

```json
{
  "somePropName": "83280023120240014208594594"
}
```

### ID

NO PROPERTIES

> :notebook_with_decorative_cover: **Description:** This special input data is only for generating ids when you have
> object inside of array. <br>
> It is really useful if you want to have some unique value for every project. It will always return values 1, 2, 3 and
> so
> on.

Example input in file:

```json
[
  {
    "$iteration$": 7,
    "id": "ID"
  }
]
```

Result:

```json
[
  {
    "id": 1
  },
  {
    "id": 2
  },
  {
    "id": 3
  },
  {
    "id": 4
  },
  {
    "id": 5
  },
  {
    "id": 6
  },
  {
    "id": 7
  }
]
```

### POSTCODE

> :notebook_with_decorative_cover: **Description:** It generates post code in format `\d{2}-\d{3}`.

NO PROPERTIES

Example input file:

```json
{
  "somePropName": "POSTCODE"
}
```

Result:

```json
{
  "somePropName": "75-244"
}
```

### STREET

> :notebook_with_decorative_cover: **Description:** It generates street from list of available streets in Poland with
> building number.

NO PROPERTIES

Example input file:

```json
{
  "somePropName": "STREET"
}
```

Result:

```json
{
  "somePropName": "Przyjaźni 5"
}
```

### CITY

> :notebook_with_decorative_cover: **Description:** It generates city from list of available cities in Poland.

| **PROPERTY NAME** |     **DESCRIPTION**      |          **POSSIBLE VALUE**          |
|:-----------------:|:------------------------:|:------------------------------------:|
|    **startAt**    | First letter of the city | every letter (a-z), case insensitive |

**Example:**

Input file with correct values:

```json
{
  "somePropName": "CITY|startAt=a"
}
```

**Result:**

```json
{
  "somePropName": "Aleksandrów Kujawski"
}
```

Input file with incorrect values:

```json
{
  "somePropName": "CITY|startAt=$"
}
```

**Result:**

```json
{
  "somePropName": "Warszawa"
}
```

### VOIVODESHIP

> :notebook_with_decorative_cover: **Description:** It generates voivodeship from the list of available voivodeships in
> Poland.

NO PROPERTIES

Example input file:

```json
{
  "somePropName": "VOIVODESHIP"
}
```

Result:

```json
{
  "somePropName": "Mazowieckie"
}
```

### COUNTY

> :notebook_with_decorative_cover: **Description:** It generates county from the list of available counties in Poland.

NO PROPERTIES

Example input file:

```json
{
  "somePropName": "COUNTY"
}
```

Result:

```json
{
  "somePropName": "Bytom"
}
```

### ADDRESS

> :notebook_with_decorative_cover: **Description:** It generates object that is a combination
> of `POSTCODE`, `STREET`, `CITY`, `VOIVODESHIP` and `COUNTY`.

|    **PROPERTY NAME**    |        **DESCRIPTION**        |           **POSSIBLE VALUE**            |
|:-----------------------:|:-----------------------------:|:---------------------------------------:|
|    **cityPropName**     |    Property name for city     | Any valid string for key in json object |
|   **streetPropName**    |   Property name for street    | Any valid string for key in json object |
|  **postcodePropName**   |  Property name for postcode   | Any valid string for key in json object |
| **voivodeshipPropName** | Property name for voivodeship | Any valid string for key in json object |
|   **countyPropName**    |   Property name for county    | Any valid string for key in json object |

**Example:**

Input file with all properties:

```json
{
  "somePropName": "ADDRESS|cityPropName=cityKeyName,streetPropName=streetKeyName,postcodePropName=postcodeKeyName,voivodeshipPropName=voivodeshipKeyName,countyPropName=countyKeyName"
}
```

**Result:**

```json
{
  "somePropName": {
    "cityKeyName": "Bydgoszcz",
    "streetKeyName": "Młyńska 167",
    "postcodeKeyName": "85-226",
    "voivodeshipKeyName": "Kujawsko-pomorskie",
    "countyKeyName": "Bydgoszcz"
  }
}
```

> :warning: **WARNING:** If you set the `ADDRESS` properties with empty strings like
> this `ADDRESS|cityPropName=,streetPropName=, postcodePropName=,voivodeshipPropName=,countyPropName=` then you'll get
> empty object (`{}`)

# GOTCHA

You **HAVE TO** remember about setting `iterationNumber` argument properly if it is important to you. If you set
new iteration number key in json file, but you forgot to add it in arguments then you'll get default value (10) apart
from
whether value will be set correctly. If you added new iteration name in json, you set it via arguments correctly **BUT**
you set the value not as a number, then the iteration property will be not omitted in the result and also the iteration
number will be set on default (10).

# TODO

- [x] Adding `Dockerfile`
- [ ] Adding possibility to use name of `Special Input Data` but like a typical regex
- [ ] Creating UI and API for using application in the browser
