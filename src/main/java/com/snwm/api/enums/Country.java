package com.snwm.api.enums;

import java.util.Arrays;
import java.util.List;

public enum Country {
    AUSTRIA("at", Arrays.asList(Operator.A1, Operator.LYCAMOBILE, Operator.ORANGE, Operator.TELERING)),
    BULGARIA("bg",
            Arrays.asList(Operator.A1, Operator.BULSATCOM, Operator.MAX_TELECOM, Operator.TELENOR, Operator.VIVACOM)),
    CZECH_REPUBLIC("cz",
            Arrays.asList(Operator.NORDIC_TELECOM, Operator.O2, Operator.SZDC, Operator.TMOBILE, Operator.VODAFONE)),
    DENMARK("dk", Arrays.asList(Operator.LYCAMOBILE)),
    ESTONIA("ee", Arrays.asList(Operator.ELISA, Operator.TELIA, Operator.TELE2)),
    FINLAND("fi", Arrays.asList(Operator.DNA, Operator.ELISA)),
    FRANCE("fr", Arrays.asList(Operator.DNA, Operator.ELISA)),
    GEORGIA("ge", Arrays.asList(Operator.BEELINE, Operator.GEOCELL, Operator.MAGTICOM, Operator.SILKNET)),
    GERMANY("de", Arrays.asList(Operator.LEBARA, Operator.LYCAMOBILE, Operator.VODAFONE)),
    GREECE("gr", Arrays.asList(Operator.COSMOTE, Operator.CYTA, Operator.VODAFONE, Operator.WIND)),
    HONG_KONG("hk",
            Arrays.asList(Operator.CHINA_MOBILE, Operator.CSL, Operator.PCCW, Operator.SMARTONE, Operator.THREE)),
    INDONESIA("id",
            Arrays.asList(Operator.AXIS, Operator.INDOSAT, Operator.SMARTFREN, Operator.TELKOMSEL, Operator.THREE)),
    KAZAKHSTAN("kz",
            Arrays.asList(Operator.ACTIV, Operator.ALTEL, Operator.BEELINE, Operator.FORTE_MOBILE, Operator.TELE2)),
    KENYA("ke", Arrays.asList(Operator.AIRTEL, Operator.ECONET, Operator.ORANGE, Operator.SAFARICOM, Operator.TELKOM)),
    KYRGYZSTAN("kg", Arrays.asList(Operator.BEELINE, Operator.MEGACOM, Operator.O_PLUS)),
    LAOS("la", Arrays.asList(Operator.BEELINE, Operator.ETL, Operator.LAOTEL, Operator.UNITEL)),
    LATVIA("lv", Arrays.asList(Operator.LMT, Operator.TELE2)),
    LITHUANIA("lt", Arrays.asList(Operator.LABAS, Operator.PYLDUK, Operator.TELE2)),
    MALAYSIA("my",
            Arrays.asList(Operator.CELCOM, Operator.DIGI, Operator.ELECTCOMS, Operator.INDOSAT, Operator.MAXIS,
                    Operator.TELEKOM, Operator.TUNE_TALK, Operator.U_MOBILE, Operator.YES)),
    MEXICO("mx", Arrays.asList(Operator.TELCEL)),
    MOLDOVA("md", Arrays.asList(Operator.MOLDCELL, Operator.ORANGE)),
    MOROCCO("ma", Arrays.asList(Operator.INWI, Operator.MAROC_TELECOM, Operator.ORANGE)),
    NETHERLANDS("nl", Arrays.asList(Operator.KPN, Operator.LEBARA, Operator.LMOBIEL, Operator.LYCAMOBILE)),
    PAKISTAN("pk",
            Arrays.asList(Operator.CHARJI, Operator.JAZZ, Operator.SCO_MOBILE, Operator.TELENOR, Operator.UFONE,
                    Operator.ZONG)),
    PHILLIPINES("ph", Arrays.asList(Operator.GLOBE_TELECOM, Operator.SMART, Operator.SUN_CELLULAR)),
    POLAND("pl", Arrays.asList(Operator.NJU, Operator.ORANGE, Operator.PLAY, Operator.PLUS, Operator.TMOBILE)),
    PORTUGAL("pt", Arrays.asList(Operator.LYCAMOBILE, Operator.VODAFONE)),
    ROMANIA("ro", Arrays.asList(Operator.VODAFONE)),
    RUSSIA("ru",
            Arrays.asList(Operator.BEELINE, Operator.LYCAMOBILE, Operator.MEGAFON, Operator.MTS, Operator.TELE2,
                    Operator.MTT, Operator.PATRIOT, Operator.ROSTELECOM, Operator.TINKOFF, Operator.VECTOR,
                    Operator.VTB_MOBILE, Operator.YOTA)),
    SPAIN("es", Arrays.asList(Operator.LYCAMOBILE, Operator.YOIGO)),
    SWEDEN("se", Arrays.asList(Operator.COMVIQ, Operator.LYCAMOBILE, Operator.TELENOR)),
    THAILAND("th", Arrays.asList(Operator.AIS, Operator.DTAC, Operator.TRUEMOVE)),
    UKRAINE("ua", Arrays.asList(Operator.KYIVSTAR, Operator.LIFECELL, Operator.LYCAMOBILE, Operator.VODAFONE)),
    UNITED_KINGDOM("gb",
            Arrays.asList(Operator.EE, Operator.GIFFGAFF, Operator.LYCAMOBILE, Operator.O2, Operator.ORANGE,
                    Operator.THREE, Operator.TMOBILE, Operator.VODAFONE)),
    UNITED_STATES("us", Arrays.asList(Operator.TMOBILE)),
    UZBEKISTAN("uz",
            Arrays.asList(Operator.BEELINE, Operator.PERFECTUM, Operator.UCELL, Operator.UMS, Operator.UZMOBILE)),
    VIETNAM("vn", Arrays.asList(Operator.VIETNAMOBILE, Operator.VIETTEL, Operator.VINAPHONE));

    private final String code;
    private final List<Operator> operators;

    Country(String code, List<Operator> operators) {
        this.code = code;
        this.operators = operators;
    }

    public String getCode() {
        return code;
    }

    public List<Operator> getOperators() {
        return operators;
    }
}
