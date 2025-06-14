package com.example.cdwebbackend.util;

import java.util.List;

public class BannedWordsUtil {
    private static final List<String> BANNED_WORDS = List.of(
            // Vietnamese
            "địt", "lồn", "cặc", "đéo", "buồi", "cu", "dái", "chym", "bướm",
            "óc chó", "óc lợn", "óc trâu", "óc bò", "đĩ", "con đĩ", "con chó",
            "chó", "đồ chó", "ngu", "ngu vãi", "vãi lồn", "vãi đái", "vãi cả lúa",
            "mẹ mày", "bố mày", "bố láo", "bố đời", "cút", "khốn nạn", "mẹ kiếp",
            "vkl", "vl", "vcl", "vãi lúa", "clm", "clgt", "dm", "dmm", "vđ", "tổ cha mày",
            "đm", "ml", "vcc", "vcd", "lgbt",

            // English
            "fuck", "shit", "bitch", "asshole", "dick", "pussy", "bastard", "fucker",
            "motherfucker", "son of a bitch", "cunt", "crap", "jerk", "slut",
            "whore", "damn", "retard", "moron", "suck my", "screw you", "stfu", "wtf"
    );
    public static boolean containsBannedWords(String content) {
        if (content == null) return false;
        String normalized = content.toLowerCase();
        return BANNED_WORDS.stream().anyMatch(normalized::contains);
    }
}
