package sen.khyber.misc;

import sen.khyber.stats.counter.WordCounter;

public class Misc {
    
    public static void main(final String[] args) {
        final WordCounter essay = new WordCounter();
        essay.addWords(
                "The Fgrandmother is not definitively right to spank Sophie, but compared to the "
                + "alternative put forth by Sophie’s parents, only modeling and teaching without "
                + "any effective discipline, it is the lesser of two evils, because occasional "
                + "spanking has immediate benefits and is unlikely to permanently traumatize a "
                + "three-year-old.\r\n\r\n\r\nSophie’s parents raise her without any apparent "
                + "discipline, opting to only use teaching and modeling.  They “agree Sophie is a "
                + "problem, but they don’t know what to do,” demonstrating that they acknowledge "
                + "the need to change Sophie’s behavior but fail to discipline her in any way (9).  "
                + "When the grandmother suggests spanking, the parents scold her, saying, “Use your "
                + "words… That’s what we tell Sophie.  How about if you set a good example” (9).  "
                + "Evidently, Sophie’s parents see pure modeling as a better alternative to "
                + "spanking or other physical methods of discipline.  They think “it is better to "
                + "explain to Sophie that clothes are a good idea,” yet this clearly does not work "
                + "because Sophie continues to defiantly take her clothes off (9).  But not only "
                + "do they oppose spanking as a disciplinary method, there is no evidence of them "
                + "using any form of discipline.\r\nWith Sophie’s parents failing to discipline "
                + "her in any way, the grandmother is left with a dilemma: Should she follow John "
                + "and Natalie’s advice and thus give in to Sophie’s rebellion.  Or should she end "
                + "Sophie’s rebellious streak (pun intended) by using spanking?  She chooses the "
                + "latter, and this decision ultimately proves to be effective—to an extent.  At "
                + "first, she tries to use verbal discipline, saying “Sophie, if you take off your "
                + "clothes, no snack,” “Sophie, if you take off your clothes, no lunch,” and "
                + "“Sophie, if you take off your clothes, no park” (9-10).  Evidently, she "
                + "attempts to use her words, but Sophie proves too stubborn, and thus the "
                + "grandmother realizes she must resort to harsher discipline.  After “one day I "
                + "[the grandmother] spank her[,]...the next day...she does not take off her "
                + "clothes,” clearly showing the blunt effectiveness of spanking, which is "
                + "monumentally more than that of John and Natalie’s nonexistent discipline "
                + "(10).\r\n\r\n\r\nThere is no substantial evidence in the story to show Sophie "
                + "is emotionally scarred by the spanking, and it is unlikely that occasional "
                + "spanking will traumatize a three-year-old.");
        System.out.println(essay.toBarGraph().toString());
        System.out.println();
        essay.sortedCounts().forEach(System.out::println);
    }
    
}
