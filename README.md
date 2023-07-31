# RegexDataGenerator

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=plastic)](https://opensource.org/licenses/Apache-2.0)
[![codecov](https://codecov.io/gh/LuckyBOYZ/RegexDataGenerator/branch/main/graph/badge.svg)](https://codecov.io/gh/LuckyBOYZ/RegexDataGenerator)

# Table of contents

[Description](https://github.com/LuckyBOYZ/RegexDataGenerator#description) <br>
[Requirements](https://github.com/LuckyBOYZ/RegexDataGenerator#requirements) <br>
[How To Run](https://github.com/LuckyBOYZ/RegexDataGenerator#how-to-run) <br>
[How To Use](https://github.com/LuckyBOYZ/RegexDataGenerator#how-to-use) <br>
[Special Input Data](https://github.com/LuckyBOYZ/RegexDataGenerator#special-input-data) <br>
[TODO](https://github.com/LuckyBOYZ/RegexDataGenerator#todo) <br>
[GOTCHA](https://github.com/LuckyBOYZ/RegexDataGenerator#gotcha)

# Description

Application let you save a time on creating dummy data for example when you want to use `json-server`
to create API for your front-end application. <br>
In short: RegexDataGenerator is using regular expressions to generate json file based on regular expression or by
special property value to generate random real data (like PESEL or polish names).

> :notebook_with_decorative_cover: **WORTH TO MENTION:** every available syntax for regular expressions you can
> check [here](https://github.com/curious-odd-man/RgxGen#supported-syntax)
> and every limitation is available [here](https://github.com/curious-odd-man/RgxGen#limitations)

# Requirements

To run this application you have to install `java 17` on your machine.

# How To Run

At the moment I didn't prepare any `Dockerfile` or API so the only one way for using it is run it on your local machine
by command <br>
`java -jar RegexDataGenerator.jar <ARGUMENTS>`. The latest version of application is put into `bin` directory. There are few
arguments which you can add to command to customize how the application must work (but you don't need):

|       **ARGUMENT NAME**       |                                                                        **DESCRIPTION**                                                                        |                   **VALUES**                    | **DEFAULT VALUE** |
|:-----------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------:|:-----------------:|
|    **iterationFieldName**     |                 name of 'how many objects have to be created in array' key property in json. This prop is **only** used for objects in array.                 |        every valid name for key in json         |     iteration     |
|      **iterationNumber**      |                                                            value for `iterationFieldName` argument                                                            |            every number more than 0             |        10         |
|       **jsonFileName**        | name of input file - it can be path but file **HAS TO** be inside the same folder as application (e.g. dummy/test.jar and file is under dummy/test/file.json) | every valid path to file with `.json` extension |     file.json     |
|      **prefixAndSuffix**      |     character which is used before and after special keys. At the moment this property is used only for creating `iterationFieldName` as a key in object      |                 every character                 |         $         |
| **specialInputDataSeparator** |                                               separator between `SpecialInputData` name and properties for this                                               |                 every character                 |        \|         |
|     **isFormattedResult**     |                                               whether the result must be formatted (multiline and indentations)                                               |                `true` or `false`                |      `false`      |

Those arguments **HAVE TO** be seperated by `=`
e.g. `java -jar RegexDataGenerator.jar jsonFileName=test.json iterationFieldName=it` <br>
The result will be always in the same directory as the application is run.

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

Apart of `$iteration$` every properties keys are up to you (this property will be explained later). <br>
The value for those properties can be typical regex, or you can
use [Special Input Data](https://github.com/LuckyBOYZ/RegexDataGenerator#special-input-data) name. <br>
You can use either `array` and `object` as a root in config file. <br>
Below is an overview with the most important information about creating configuration for every data type:

### string

You can use regular expression, some hardcoded value (e.g. `abcd`) or `Special Input Data` name (please check **table of
contents**)

### object

The name for every property is up to you apart of `$iteration$`. This property tells application how many objects must
be created, and it works **ONLY** if the object is inside of array.

### array

Array requires 1 element (string or object) but can be used second optional element - how many elements application must
create (value must be the number). If the second element is not a number the default value for iteration (10) will be
used.
String and object must be created in the same way as it is described above.

# Special Input Data

You can use special values to create some random data but without regular expression. <br>
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

There are 2 ways to use special values:

1. use only name of special value (e.g. `SURNAME`)
2. use name of special value with parameters. If some special input data has parameters, then after special name **MUST
   ** be used pipe -> `|`. You can use only some parameters instead of all of them.

> :warning: **WARNING:** Please be aware that using `|` without any property after this will cause that this value will
> be omitted in the result!
> Here are all `Special Input Data` values and theirs configuration:

> :warning: **WARNING:** When you're using properties with `Special Input Data` you **MUST** use them like _key_=_value_
> and every property must be seperated by comma (`,`) from another one like this
> _key1_=_value1_,_key2_=_value2_ <br>
> You can't use empty string as a kay or value because property will be omitted in the result.


Here are all `Special Input Data` values and theirs configuration:

### NAME

> :notebook_with_decorative_cover: **Description:** It generates one of the name that is used in Poland. If application
> cannot find any value for parameters then is returned `Jan` for male and `Janina` for female

| **PROPERTY NAME** |         **DESCRIPTION**         |          **POSSIBLE VALUE**          |
|:-----------------:|:-------------------------------:|:------------------------------------:|
|    **startAt**    |    First letter of the name     | every letter (a-z), case insensitive |
|    **female**     | Does a name must be for female? |             true, false              |

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

Input startAt but female with `true` value:

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
|    **female**     | Does a surname must be for female? |             true, false              |

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

Input startAt but female with `true` value:

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

> :notebook_with_decorative_cover: **Description:** It generates PESEL number. PESEL is like an id for every person in
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

| **PROPERTY NAME** |                           **DESCRIPTION**                           |         **POSSIBLE VALUE**         |
|:-----------------:|:-------------------------------------------------------------------:|:----------------------------------:|
|    **country**    |             Country from table with available countries             |      Available Country Table       |
|   **bankName**    |                Bank name from table with bank names                 | Bank Name Table for chosen country |
|   **formatted**   | Does iban have to be splitted by space between every 4th character? |            true, false             |
|  **withLetters**  |          Does iban must contain letters at the beginning?           |            true, false             |

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
> Is really useful if you want to have some unique value for every project. It will always return values 1, 2, 3 and so
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

- [ ] Adding `Dockerfile`
- [ ] Adding possibility to use name of `Special Input Data` but like a typical regex
- [ ] Creating UI and API for using application in the browser
