package wonderpets.model;

import java.util.List;

/**
 * Immutable data object representing dietary guidance associated with a pet symptom.
 * All list fields are defensively copied on construction and on access.
 */
public class SymptomEntry {

    private final String symptom;
    private final List<String> eatMore;
    private final List<String> eatLess;
    private final List<String> increaseNutrients;
    private final List<String> monitorNutrients;

    /**
     * Constructs a {@code SymptomEntry}.
     *
     * @param symptom           the symptom name; must be non-null and non-blank
     * @param eatMore           foods to eat more of
     * @param eatLess           foods to eat less of
     * @param increaseNutrients nutrients to increase
     * @param monitorNutrients  nutrients to monitor
     * @throws IllegalArgumentException if {@code symptom} is null or blank
     */
    public SymptomEntry(String symptom,
                        List<String> eatMore,
                        List<String> eatLess,
                        List<String> increaseNutrients,
                        List<String> monitorNutrients) {
        if (symptom == null || symptom.isBlank()) {
            throw new IllegalArgumentException("symptom must be non-null and non-blank");
        }
        this.symptom           = symptom;
        this.eatMore           = List.copyOf(eatMore);
        this.eatLess           = List.copyOf(eatLess);
        this.increaseNutrients = List.copyOf(increaseNutrients);
        this.monitorNutrients  = List.copyOf(monitorNutrients);
    }

    /**
     * Returns the symptom name.
     *
     * @return the symptom
     */
    public String getSymptom() {
        return symptom;
    }

    /**
     * Returns a copy of the foods recommended to eat more of.
     *
     * @return list of foods to eat more of
     */
    public List<String> getEatMore() {
        return List.copyOf(eatMore);
    }

    /**
     * Returns a copy of the foods recommended to eat less of.
     *
     * @return list of foods to eat less of
     */
    public List<String> getEatLess() {
        return List.copyOf(eatLess);
    }

    /**
     * Returns a copy of the nutrients recommended to increase.
     *
     * @return list of nutrients to increase
     */
    public List<String> getIncreaseNutrients() {
        return List.copyOf(increaseNutrients);
    }

    /**
     * Returns a copy of the nutrients to monitor.
     *
     * @return list of nutrients to monitor
     */
    public List<String> getMonitorNutrients() {
        return List.copyOf(monitorNutrients);
    }
}
