package wonderpets.util;

import wonderpets.model.SymptomEntry;
import wonderpets.model.SymptomIndex;

import java.util.List;

/**
 * Populates a {@link SymptomIndex} with a set of hardcoded dietary guidance records.
 * Call {@link #seed(SymptomIndex)} once at application startup before the index
 * is shown to the user.
 */
public final class SeedData {

    private SeedData() {}

    /**
     * Adds twelve symptom entries with realistic dietary guidance to {@code index}.
     *
     * @param index the index to populate; must not be {@code null}
     */
    public static void seed(SymptomIndex index) {

        index.addEntry(new SymptomEntry(
                "Fatigue",
                List.of("Lean red meat", "Eggs", "Oats", "Spinach", "Lentils",
                        "Sweet potatoes", "Bananas"),
                List.of("Refined sugar", "White bread", "Alcohol",
                        "Fried foods", "Energy drinks"),
                List.of("Iron", "Vitamin B12", "Magnesium", "CoQ10",
                        "Vitamin D"),
                List.of("Ferritin", "Thyroid hormones", "Blood glucose",
                        "Vitamin D level")
        ));

        index.addEntry(new SymptomEntry(
                "Bloating",
                List.of("Ginger", "Kefir", "Yogurt", "Cucumber",
                        "Bananas", "Papaya", "Fennel"),
                List.of("Beans", "Broccoli", "Cabbage", "Carbonated drinks",
                        "Chewing gum", "Onions", "Dairy"),
                List.of("Probiotics", "Digestive enzymes", "Zinc"),
                List.of("FODMAP intake", "Lactose tolerance", "Fibre total")
        ));

        index.addEntry(new SymptomEntry(
                "Brain fog",
                List.of("Fatty fish", "Blueberries", "Walnuts",
                        "Dark leafy greens", "Avocado", "Dark chocolate",
                        "Green tea"),
                List.of("Refined sugar", "Processed foods", "Alcohol",
                        "Trans fats", "Artificial sweeteners"),
                List.of("Omega-3", "Vitamin B complex", "Vitamin E",
                        "Choline"),
                List.of("Blood glucose", "Hydration", "Thyroid function",
                        "Vitamin B12")
        ));

        index.addEntry(new SymptomEntry(
                "Insomnia",
                List.of("Kiwi", "Tart cherries", "Almonds", "Turkey",
                        "Chamomile tea", "Oats", "Warm milk"),
                List.of("Caffeine", "Alcohol", "Spicy foods",
                        "High-fat meals", "Sugary snacks before bed"),
                List.of("Magnesium", "Tryptophan", "Melatonin",
                        "Vitamin B6"),
                List.of("Caffeine intake", "Alcohol consumption",
                        "Blood sugar stability")
        ));

        index.addEntry(new SymptomEntry(
                "Inflammation",
                List.of("Fatty fish", "Berries", "Extra-virgin olive oil",
                        "Turmeric", "Ginger", "Dark leafy greens",
                        "Green tea"),
                List.of("Refined carbohydrates", "Fried foods",
                        "Margarine", "Red meat", "Processed snacks",
                        "Alcohol"),
                List.of("Omega-3", "Vitamin C", "Vitamin E",
                        "Polyphenols", "Curcumin"),
                List.of("CRP (C-reactive protein)", "Trans fat intake",
                        "Arachidonic acid", "Omega-6 to omega-3 ratio")
        ));

        index.addEntry(new SymptomEntry(
                "Anxiety",
                List.of("Salmon", "Yogurt", "Dark chocolate",
                        "Blueberries", "Chamomile tea", "Almonds",
                        "Leafy greens"),
                List.of("Caffeine", "Alcohol", "Refined sugar",
                        "Processed foods", "High-sodium foods"),
                List.of("Magnesium", "Omega-3", "Zinc",
                        "Vitamin B complex", "L-theanine"),
                List.of("Caffeine intake", "Blood sugar levels",
                        "Gut microbiome health")
        ));

        index.addEntry(new SymptomEntry(
                "Constipation",
                List.of("Prunes", "Pears", "Apples", "Flaxseed",
                        "Lentils", "Oats", "Broccoli", "Water"),
                List.of("Dairy products", "Red meat", "Refined grains",
                        "Processed foods", "Unripe bananas"),
                List.of("Dietary fibre", "Magnesium", "Water intake",
                        "Probiotics"),
                List.of("Total daily fibre", "Fluid intake",
                        "Bowel transit time")
        ));

        index.addEntry(new SymptomEntry(
                "High blood pressure",
                List.of("Bananas", "Sweet potatoes", "Beets",
                        "Leafy greens", "Oats", "Berries",
                        "Low-fat dairy"),
                List.of("Salt", "Processed foods", "Red meat",
                        "Alcohol", "Caffeine", "Pickled foods"),
                List.of("Potassium", "Magnesium", "Calcium",
                        "Dietary nitrates"),
                List.of("Sodium intake", "Saturated fat",
                        "Alcohol units per week")
        ));

        index.addEntry(new SymptomEntry(
                "Anaemia",
                List.of("Lean red meat", "Chicken liver", "Spinach",
                        "Lentils", "Tofu", "Fortified cereals",
                        "Vitamin C-rich foods"),
                List.of("Coffee with meals", "Tea with meals",
                        "Calcium-rich foods alongside iron sources",
                        "Alcohol"),
                List.of("Iron", "Vitamin B12", "Folate", "Vitamin C"),
                List.of("Serum ferritin", "Haemoglobin",
                        "Vitamin B12 level", "Folate level")
        ));

        index.addEntry(new SymptomEntry(
                "Joint pain",
                List.of("Fatty fish", "Extra-virgin olive oil", "Berries",
                        "Broccoli", "Garlic", "Ginger", "Turmeric"),
                List.of("Processed foods", "Refined sugar",
                        "Saturated fats", "Alcohol",
                        "High-purine foods"),
                List.of("Omega-3", "Vitamin D", "Glucosamine",
                        "Collagen", "Vitamin C"),
                List.of("Uric acid", "Inflammatory markers",
                        "Vitamin D level", "Body weight")
        ));

        index.addEntry(new SymptomEntry(
                "Skin problems",
                List.of("Fatty fish", "Avocado", "Sweet potatoes",
                        "Broccoli", "Tomatoes", "Walnuts",
                        "Green tea"),
                List.of("Dairy products", "Refined sugar",
                        "Processed foods", "Alcohol",
                        "High-glycaemic foods"),
                List.of("Vitamin A", "Vitamin C", "Vitamin E",
                        "Zinc", "Omega-3"),
                List.of("Zinc level", "Vitamin D", "Biotin",
                        "Skin hydration")
        ));

        index.addEntry(new SymptomEntry(
                "Nausea",
                List.of("Ginger tea", "Plain crackers", "Bananas",
                        "White rice", "Toast", "Clear broth",
                        "Cold water"),
                List.of("Spicy foods", "Fatty foods",
                        "Strong-smelling foods", "Dairy",
                        "Caffeine", "Alcohol"),
                List.of("Vitamin B6", "Ginger", "Electrolytes"),
                List.of("Hydration", "Electrolyte balance",
                        "Trigger foods")
        ));
    }
}
