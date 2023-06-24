package com.lukaszsuma.regexdatagenerator.utils;

public enum PolandBankId {
	NBP("1010"),
	PKOBP("1020"),
	CITI("1030"),
	ING("1050"),
	SANTANDER_PL("1090"),
	BGK("1130"),
	MBANK("1140"),
	MILLENNIUM("1160"),
	PKOSA("1240"),
	POCZTOWY("1320"),
	BOS("1540"),
	MERCEDES("1580"),
	SGB("1610"),
	PLUS("1680"),
	SOCIETE("1840"),
	NEST("1870"),
	BPS("1930"),
	AGRICOLE("1940"),
	BNP("2030"),
	SANTANDER_CS("2120"),
	TOYOTA("2160"),
	DNB("2190"),
	ALIOR("2490"),
	FCE("2710"),
	INBANK("2720"),
	VOLKSWAGEN("2770"),
	HSBC("2800"),
	BFF("2850"),
	AION("2910"),
	VELO("2930");

    private final String id;

    PolandBankId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
