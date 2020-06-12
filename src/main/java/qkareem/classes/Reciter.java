package qkareem.classes;

import org.json.JSONObject;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class Reciter {
    public int id;
    public String name;
    public String server;
    public String rewaya;
    public int count;
    public String letter;
    public String suras;

    public Reciter(JSONObject reciterJson) {
        this.id = reciterJson.getInt("id");
        this.name = reciterJson.getString("name");
        this.server = reciterJson.getString("server");
        this.rewaya = reciterJson.getString("rewaya");
        this.count = reciterJson.getInt("count");
        this.letter = reciterJson.getString("letter");
        this.suras = ","+reciterJson.getString("suras")+",";
    }

    public boolean hasSurah(int surahId) {
        return this.suras.indexOf(","+surahId+",") >= 0;
    }

    public boolean recitesRewaya(String rewaya) {
        System.out.println(FuzzySearch.partialRatio(rewaya, this.rewaya) + ": " + rewaya);
        return FuzzySearch.partialRatio(rewaya, this.rewaya) >= 100;
    }

    public String getSurahMp3Url(int surahId) {
        String surahName = "" + surahId;
        while (surahName.length() < 3) {
            surahName = "0" + surahName;
        }
        return this.server + "/" + surahName + ".mp3";
    }
}