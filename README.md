This Java library extends commons-validator and implements algorithms specified in [ISO/IEC 7064](https://en.wikipedia.org/wiki/ISO/IEC_7064) and [LUHN](https://en.wikipedia.org/wiki/Luhn_algorithm) which is defined in [ISO/IEC 7812-1 Annex B](https://en.wikipedia.org/wiki/ISO%2FIEC_7812).

The standard-validator jar calculates/validates

- credit card numbers ([LUHN](https://github.com/homebeaver/ungueltig/wiki/LUHN))
- travel document numbers (passports and ID-Cards, miles&more)
- driver license numbers
- rail card numbers
- all [IBAN](https://en.wikipedia.org/wiki/International_Bank_Account_Number)s (ISO/IEC 7064:MOD 97-10)
- [US Routing transit number](https://en.wikipedia.org/wiki/Routing_transit_number) ([MOD-10](https://github.com/homebeaver/ungueltig/wiki/MOD-10#371))
- [Creditor Reference](https://en.wikipedia.org/wiki/Creditor_Reference) (ISO-11649)
- [ISIN](https://en.wikipedia.org/wiki/International_Securities_Identification_Number) (US-CUSIP and GB-SEDOL)
- EAN/UPC, ISBNs and ISSNs (MOD-10)
- all european [VAT ID numbers](https://en.wikipedia.org/wiki/VAT_identification_number) and Tax numbers 
- french [SIREN](https://en.wikipedia.org/wiki/SIREN_code) and SIRET numbers (LUHN)
- chemical numbers [CAS](https://en.wikipedia.org/wiki/CAS_Registry_Number), EC index and [EC-number](https://en.wikipedia.org/wiki/European_Community_number)
- [IMO vessel number](https://en.wikipedia.org/wiki/IMO_number) ([MOD-10](https://github.com/homebeaver/ungueltig/wiki/MOD-10))
- german [Leitweg-ID](https://de.wikipedia.org/wiki/Leitweg-ID) (ISO/IEC 7064:MOD 97-10)
- [Legal Entity Identifier](https://en.wikipedia.org/wiki/Legal_Entity_Identifier) (ISO-17442)

ISO/IEC 7064
============

calculates/validates [ISO/IEC 7064](https://github.com/homebeaver/ungueltig/wiki/ISO-IEC_7064) check characters.

The algorithms include all **pure systems** plus the pure polynomial implementations of:
* [ISO/IEC 7064, MOD 11-2](https://github.com/homebeaver/ungueltig/wiki/MOD-11-2) for numeric strings with one check digit or the supplementary check character "X"
* [ISO/IEC 7064, MOD 37-2](https://github.com/homebeaver/ungueltig/wiki/MOD-37-2) for alphanumeric strings with one check digit or letter or the supplementary check character "*"
* [ISO/IEC 7064, MOD 97-10](https://github.com/homebeaver/ungueltig/wiki/MOD-97-10) for numeric strings with two check digits
* ISO/IEC 7064, MOD 661-26 for alphabetic strings with two check letters
* ISO/IEC 7064, MOD 1271-36 for alphanumeric strings with two check digits or letters

This is a full implementation of the ISO/IEC standard. Also **hybrid systems** are implemeted:
* [ISO/IEC 7064, MOD 11,10](https://github.com/homebeaver/ungueltig/wiki/MOD-11%2C10) for numeric strings with one check digit
* ISO/IEC 7064, MOD 27,26 for alphabetic strings with one check letter
* [ISO/IEC 7064, MOD 37,36](https://github.com/homebeaver/ungueltig/wiki/MOD-37%2C36) for alphanumeric strings with one check digit or letter


ICAO MOD 10 standard
====================

As defined in [ICAO Doc9003](https://www2023.icao.int/publications/Documents/9303_p3_cons_en.pdf) for travel documents like passports and ID-Cards.


LUHN
====

There is no standard for the [Luhn algorithm](https://en.wikipedia.org/wiki/Luhn_algorithm). It is specified in Annex of ISO/IEC 7812 for Identification cards. Here a screenshot:

![](https://raw.githubusercontent.com/wiki/homebeaver/ungueltig/image/LUHN.png)

How to use
==========

```xml
<!-- https://mvnrepository.com/artifact/io.github.homebeaver/standard-validator -->
<dependency>
    <groupId>io.github.homebeaver</groupId>
    <artifactId>standard-validator</artifactId>
    <version>2.10.4</version>
</dependency>
```

The modules are originally planned to be part of [Apache commons-validator](https://commons.apache.org/proper/commons-validator/download_validator.cgi)

Java example:

```java
CheckDigit numericCheck = IsoIecPure11System.getInstance();

String checkDigit = numericCheck.calculate("123456");
boolean isValid = numericCheck.isValid("123456X");
```

A more practical example (create a valid Structured Creditor Reference and check it):

```java
String createRFCreditorReference(final String cr) {
    System.out.println("createRFCreditorReference from [" + cr + "]");
    CheckDigit checkDigit = RFCreditorReferenceCheckDigit.getInstance();

    String cd = "00"; // check digit to start with
    try {
        cd = checkDigit.calculate(RFCreditorReferenceCheckDigit.RF + cd + cr);
        return RFCreditorReferenceCheckDigit.RF + cd + cr;
    } catch (CheckDigitException ex) {
        System.out.println("failed to create RFCreditorReference : " + ex.getMessage());
    }
    return null;
}

    final String cr = "InvNo4711date25DEC31"; //Maximum is 21 chars
    String validRFcr = createRFCreditorReference(cr);
    boolean isValid = RFCreditorReferenceValidator.getInstance().isValid(validRFcr);
    System.out.println("created RFCreditorReference " + validRFcr + " is " + (isValid ? "OK" : "NOT valid"));

/* result is:
createRFCreditorReference from [InvNo4711date25DEC31]
created RFCreditorReference RF07InvNo4711date25DEC31 is OK
 */
```

