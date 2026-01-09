package org.apache.commons.validator.routines;

/**
 * A collection of IINs aka BINs.
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.5
 */
// Fields in interfaces and annotations are automatically public static final
public interface IssuerIdentificationNumber {


/*
----+---
708406       Q8LIBY
740505
740506
// Prepaid:
...
6393560      3VGER 3V (Visa, Deutschland)
...
95285007     DALLMA Dallmayr Lukullus Kundenkarte
...
7018         APAPC Shell APAPC Karte
...
700414190    VPOWCL Shell V-Power Club
700414191    VPOWCA Shell V-Power Club ADAC
700499       ADAC
710521       LHFRIE Lufthansa & Friends
700063142    SHPRPC
...
000000F982   TURKDE
 */

/* pseudo Prefix
----+---
00000090     193 / X'00C1' CCRECO  
000000ACC0
000000nn00   einige Prepaid-Karten mit zweistelliger dezimaler Kartenart-Id nn 
000000Fnnn   Prepaid-Karten mit dezimaler Kartenart-Id nnn 
900077777777 222 / X'00DE' TAXFEU  
7777777 
900088888888 221 / X'00DD' TAXFWW  
8888888 
900099999999 143 / X'008F' TAXFRE 
9999999 
999990       Euro-ELV im Internet 
99999101     68 / X'0044' BAR Default-Präfix 
99999102     134 / X'0086' CASH Default-Präfix 
999999       reserviert 
 */
}
