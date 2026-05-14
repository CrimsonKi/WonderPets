package wonderpets;

import wonderpets.controller.AdminController;
import wonderpets.controller.SearchController;
import wonderpets.model.SymptomIndex;
import wonderpets.util.SeedData;
import wonderpets.view.AdminView;
import wonderpets.view.SearchView;

import javax.swing.SwingUtilities;

/**
 * Application entry point.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SymptomIndex index = new SymptomIndex();
            SeedData.seed(index);

            SearchController searchCtrl = new SearchController(index, null);
            AdminController  adminCtrl  = new AdminController(index, null);

            AdminView  adminView  = new AdminView(index, adminCtrl);
            SearchView searchView = new SearchView(index, searchCtrl, adminView);

            searchView.setVisible(true);
        });
    }
}
