package net.serenitybdd.demo.pages;


import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import net.serenitybdd.demo.model.ListingItem;
import org.openqa.selenium.support.FindBy;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchResultsPage extends PageObject {

    @FindBy(css="#search-header .result-count")
    WebElementFacade resultCountSummary;

    public String getSearchHeader() {
        return $("#search-header h1").getText();
    }

    Pattern searchResultSummaryPattern = Pattern.compile("([\\d,]+) Results");

    public int getResultCount(){
        int resultCount = 0;
        Matcher resultCountMatcher = searchResultSummaryPattern.matcher(resultCountSummary.getText());
        if (resultCountMatcher.find()) {
            resultCount = parse(resultCountMatcher.group(1));
        }
        return resultCount;
    }

    public String getResultSummary(){
        return $(".summary").getText();
    }

    private int parse(String resultCount) {
        try {
            return NumberFormat.getNumberInstance(java.util.Locale.US).parse(resultCount).intValue();
        } catch (ParseException e) {
            throw new RuntimeException("Result count could not be parsed: " + resultCount);
        }
    }

    public ListingItem selectListing(int listingNumber) {
        List<WebElementFacade> listingCards = findAll(".listing-card");
        WebElementFacade listingCard = listingCards.get(listingNumber);
        String name = listingCard.findBy(".listing-title").getText();
        double price = Double.parseDouble(listingCard.findBy(".currency-value").getText());

        listingCard.findBy("a").click();

        waitForTextToAppear("Item Details");

        return new ListingItem(name, price);
    }
}
