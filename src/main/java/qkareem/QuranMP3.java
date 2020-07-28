package qkareem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import org.json.JSONArray;

import qkareem.classes.Reciter;
import qkareem.classes.Surah;

public class QuranMP3 {

    public List<Surah> suras = new ArrayList<Surah>();
    public List<Reciter> reciters = new ArrayList<Reciter>();

    public QuranMP3() throws Exception {
        String source;
        Path surasPath = Paths.get(String.format("json/%s/suras.json", Bot.lang));
        Path recitersPath = Paths.get(String.format("json/%s/reciters.json", Bot.lang));

        Bot.logger.info("Loading suras from {}", surasPath.toString());
        source = new String(Files.readAllBytes(surasPath), "UTF-8");
        JSONArray surasListJson = new JSONArray(source);

        Bot.logger.info("Loading reciters from {}", recitersPath.toString());
        source = new String(Files.readAllBytes(recitersPath), "UTF-8");
        JSONArray recitersListJson = new JSONArray(source);

        for (Object surahJson : surasListJson)
            this.suras.add(new Surah((JSONObject) surahJson));

        for (Object reciterJson : recitersListJson)
            reciters.add(new Reciter((JSONObject) reciterJson));

        this.reciters.sort(new Comparator<Reciter>() {
            @Override
            public int compare(Reciter o1, Reciter o2) {
                return o1.id - o2.id;
            }
        });
    }

    public final Reciter getReciter(String reciterName) {
        Reciter reciter = null;
        for (int i = 0; i < this.reciters.size(); i++) {
            reciter = this.reciters.get(i);
            if (reciter.name.equals(reciterName))
                break;
            else
                reciter = null;
        }
        return reciter;
    }

    public ArrayList<Reciter> searchReciters(String query, int wantPrecent) {
        ArrayList<Reciter> results = new ArrayList<>();

        if (query.chars().allMatch(Character::isDigit)) {
            int id = Integer.parseInt(query);
            for (Reciter reciter : reciters) {
                if (reciter.id == id) {
                    results.add(reciter);
                }
            }
            return results;
        }

        Collections.sort(reciters, new Comparator<Reciter>() {
            @Override
            public int compare(Reciter a, Reciter b) {

                return (FuzzySearch.partialRatio(b.name, query) * FuzzySearch.tokenSortPartialRatio(b.name, query))
                        - (FuzzySearch.partialRatio(a.name, query) * FuzzySearch.tokenSortPartialRatio(a.name, query));
            }
        });

        for (Reciter reciter : reciters) {
            int score = (FuzzySearch.partialRatio(reciter.name, query)
                    * FuzzySearch.tokenSortPartialRatio(reciter.name, query)) / 100;
            if (score < wantPrecent || results.size() >= 5 || results.contains(reciter))
                continue;
            results.add(reciter);
        }

        if (results.size() < 1)
            results.add(reciters.get(0));

        return results;
    }

    public ArrayList<Surah> searchSuras(String query, int wantPrecent) {
        ArrayList<Surah> results = new ArrayList<>();

        if (query.chars().allMatch(Character::isDigit)) {
            int id = Integer.parseInt(query);
            for (Surah surah : suras) {
                if (surah.id == id) {
                    results.add(surah);
                }
            }
            return results;
        }

        Collections.sort(suras, new Comparator<Surah>() {
            @Override
            public int compare(Surah a, Surah b) {
                return (FuzzySearch.partialRatio(b.name, query) * (FuzzySearch.tokenSortRatio(b.name, query) * 100))
                        - (FuzzySearch.partialRatio(a.name, query) * (FuzzySearch.tokenSortRatio(a.name, query) * 100));
            }
        });

        for (Surah surah : suras) {
            int score = (FuzzySearch.partialRatio(surah.name, query)
                    * (FuzzySearch.tokenSortRatio(surah.name, query) * 100)) / 100;
            if (score < wantPrecent || results.size() >= 5 || results.contains(surah))
                continue;
            results.add(surah);
        }

        if (results.size() < 1)
            results.add(suras.get(0));

        return results;
    }
}