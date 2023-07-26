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
You can use either `array` and `object` as a root in config file. <br>
Below is an overview with the most important information about creating configuration for every data type:

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
2. use name of special value with parameters. If some special input data has parameters, then after special name **MUST** be used pipe -> `|`. You can use only some parameters instead of all of them.

> :warning: **WARNING:** Please be aware that using `|` without any property after this will cause that this value will be ommited in the result!
Here are all `Special Input Data` values and theirs configuration:

> :warning: **WARNING:** When you're using properties with `Special Input Data` you **MUST** use them like _key_=_value_ and every property must be seperated by comma (`,`) from another one like this
> _key1_=_value1_,_key2_=_value2_ <br>
> You can't use empty string as a kay or value because property will be ommited in the result.


Here are all `Special Input Data` values and theirs configuration:

### NAME

> :notebook_with_decorative_cover: **Description:** It generates one of the name that is used in Poland.

| **PROPERTY NAME** |         **DESCRIPTION**         |          **POSSIBLE VALUE**          |
|:-----------------:|:-------------------------------:|:------------------------------------:|
|    **startAt**    |    First letter of the name     | every letter (a-z), case insensitive |
|    **female**     | Does a name must be for female? |             true, false              |

Examples:
1.  all properties with correect random values: `NAME|startAt=a,female=true`
2.  all properties with incorrect values: `NAME|startAt=$,female=ABCD`

### SURNAME

> :notebook_with_decorative_cover: **Description:** It generates one of the surname that is used in Poland.

| **PROPERTY NAME** |         **DESCRIPTION**            |          **POSSIBLE VALUE**          |
|:-----------------:|:----------------------------------:|:------------------------------------:|
|    **startAt**    |    First letter of the surname     | every letter (a-z), case insensitive |
|    **female**     | Does a surname must be for female? |             true, false              |

Examples:
1.  all properties with correect random values: `SURNAME|startAt=a,female=true`
2.  all properties with incorrect values: `SURNAME|startAt=$,female=ABCD`

### PESEL

> :notebook_with_decorative_cover: **Description:** It generates PESEL number. PESEL is like an id for every person in Poland.

| **PROPERTY NAME** |                    **DESCRIPTION**                     | **POSSIBLE VALUE** |
|:-----------------:|:------------------------------------------------------:|:------------------:|
| **bornAfter2000** | Does a pesel must be for person whos born after 2000?  |    true, false     |
|    **female**     |            Does a pesel must be for female?            |    true, false     |
|  **onlyAdults**   | Does application must to create pesel only for adults? |    true, false     |

Examples:
1.  all properties with correect random values: `PESEL|bornAfter2000=true,female=true,onlyAdults=true`
2.  all properties with incorrect values: `PESEL|bornAfter2000=nope,female=truthy,onlyAdults=falsy`

### IBAN

> :notebook_with_decorative_cover: **Description:** It generates valid IBAN number BUT as a default the application doesn't generate the letters at the beggining of number and without spaces (please check properties below)

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

Examples:
1.  all properties with correect random values: `IBAN|country=PL,bankName=BFF,formatted=true,withLetters=true`
2.  all properties with incorrect values: `IBAN|country=GB,bankName=UNKNOWN,formatted=abcd,withLetters=falsy`

### ID

NO PROPERTIES

> :notebook_with_decorative_cover: **Description:** This special input data is only for generating ids when you have object inside of array. <br>
> Is really useful if you want to have some unique value for every project. It will always return values 1, 2, 3 and so on.

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

### STREET

> :notebook_with_decorative_cover: **Description:** It generates street from list of available streets in Poland.

NO PROPERTIES

### CITY

> :notebook_with_decorative_cover: **Description:** It generates city from list of available cities in Poland.

| **PROPERTY NAME** |     **DESCRIPTION**      |          **POSSIBLE VALUE**          |
|:-----------------:|:------------------------:|:------------------------------------:|
|    **startAt**    | First letter of the city | every letter (a-z), case insensitive |

Examples:
1.  all properties with correect random values: `CITY|startAt=a`
2.  all properties with incorrect values: `CITY|startAt=$`

### VOIVODESHIP

> :notebook_with_decorative_cover: **Description:** It generates voivodeship from the list of available voivodeships in Poland.

NO PROPERTIES

### COUNTY

> :notebook_with_decorative_cover: **Description:** It generates county from the list of available counties in Poland.

NO PROPERTIES

### ADDRESS

> :notebook_with_decorative_cover: **Description:** It generates object that is a combination of `POSTCODE`, `STREET`, `CITY`, `VOIVODESHIP` and `COUNTY`.

|    **PROPERTY NAME**    |        **DESCRIPTION**        |           **POSSIBLE VALUE**            |
|:-----------------------:|:-----------------------------:|:---------------------------------------:|
|    **cityPropName**     |    Property name for city     | Any valid string for key in json object |
|   **streetPropName**    |   Property name for street    | Any valid string for key in json object |
|  **postcodePropName**   |  Property name for postcode   | Any valid string for key in json object |
| **voivodeshipPropName** | Property name for voivodeship | Any valid string for key in json object |
|   **countyPropName**    |   Property name for county    | Any valid string for key in json object |

Examples:
1.  all properties with correect random values: `ADDRESS|cityPropName=cityKeyName,streetPropName=streetKeyName,postcodePropName=postcodeKeyName,voivodeshipPropName=voivodeshipKeyName,countyPropName=countyKeyName`
2.  all properties with incorrect values: `ADDRESS|cityPropName=,streetPropName=,postcodePropName=,voivodeshipPropName=,countyPropName=` (empty string cannot be the key in json object)

Input file:
```json
{
  "fullPolishAddress": "ADDRESS|cityPropName=cityKeyName,streetPropName=streetKeyName,postcodePropName=postcodeKeyName,voivodeshipPropName=voivodeshipKeyName,countyPropName=countyKeyName"
}
```
Result
```json
{
  "fullPolishAddress": {
    "cityKeyName": "Bydgoszcz",
    "streetKeyName": "Tulipanowa 125",
    "postcodeKeyName": "85-485",
    "voivodeshipKeyName": "Kujawsko-pomorskie",
    "countyKeyName": "Bydgoszcz"
  }
}
```

# How To Run

# TODO

- [ ] Adding `Dockerfile`
- [ ] Adding possibility to use name of `Special Input Data` but like a typical regex
- [ ] Creating UI and API for using application in the browser
