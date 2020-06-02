package qkareem.classes;

import org.json.JSONObject;

public class Surah {
    public int id;
    public int chapterNumber;
    public boolean bismillahPre;
    public int revelationOrder;
    public String revelationPlace;
    public String nameComplex;
    public String nameArabic;
    public String nameSimple;
    public int versesCount;
    public int startPage;
    public int endPage;
    // translated_name: {
    // language_name: english,
    // name: The Opener
    // }

    public Surah(JSONObject surahJson) {
        this.id = surahJson.getInt("id");
        this.chapterNumber = surahJson.getInt("chapter_number");
        this.bismillahPre = surahJson.getBoolean("bismillah_pre");
        this.revelationOrder = surahJson.getInt("revelation_order");
        this.revelationPlace = surahJson.getString("revelation_place");
        this.nameComplex = surahJson.getString("name_complex");
        this.nameArabic = surahJson.getString("name_arabic");
        this.nameSimple = surahJson.getString("name_simple");
        this.versesCount = surahJson.getInt("verses_count");
        this.startPage = surahJson.getInt("start_page");
        this.endPage = surahJson.getInt("end_page");
    }

    
}