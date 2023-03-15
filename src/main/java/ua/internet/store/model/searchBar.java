package ua.internet.store.model;

public class searchBar {

    private String partOfWord;

    public searchBar(String partOfWord) {
        this.partOfWord = partOfWord;
    }

    public String getPartOfWord() {
        return partOfWord;
    }

    public void setPartOfWord(String partOfWord) {
        this.partOfWord = partOfWord;
    }
}
