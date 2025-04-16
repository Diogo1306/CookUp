package com.diogo.cookup.utils;

import java.util.List;

public class calculateDifficulty {

//    public static String calculateDifficulty(int prepTime, List<Ingredient> ingredients, String instructions, List<String> utensils) {
//        int score = 0;
//
//        if (prepTime > 90) score += 3;
//        else if (prepTime > 60) score += 2;
//        else if (prepTime > 30) score += 1;
//
//        if (ingredients.size() >= 15) score += 3;
//        else if (ingredients.size() >= 10) score += 2;
//        else if (ingredients.size() >= 6) score += 1;
//
//        for (Ingredient ing : ingredients) {
//            String name = ing.getName().toLowerCase();
//            if (name.contains("trufa") || name.contains("azeite") || name.contains("vinho") || name.contains("mariscos")) {
//                score += 1;
//            }
//        }
//
//        int stepCount = instructions.split("\\. ").length;
//        if (stepCount >= 10) score += 3;
//        else if (stepCount >= 6) score += 2;
//        else if (stepCount >= 3) score += 1;
//
//        for (String utensil : utensils) {
//            String lower = utensil.toLowerCase();
//            if (lower.contains("forno") || lower.contains("batedeira") || lower.contains("processador") || lower.contains("panela de pressão")) {
//                score += 1;
//            }
//        }
//
//        if (score >= 7) return "Difícil";
//        if (score >= 4) return "Médio";
//        return "Fácil";
//    }
}
