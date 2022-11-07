package org.finalbi.memegen;

import java.util.List;

public class Meme {

    boolean nsfw, spoiler;
    String postLink, subreddit, title, url, author;

    List<String> preview;

    int ups;

    @Override
    public String toString() {
        return "Meme{" +
                "nsfw=" + nsfw +
                ", spoiler=" + spoiler +
                ", postLink='" + postLink + '\'' +
                ", subreddit='" + subreddit + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", author='" + author + '\'' +
                ", preview=" + preview +
                ", ups=" + ups +
                '}';
    }

    public String getLink(){
        if (nsfw){
            return null;
        }
        return url;
    }
}
