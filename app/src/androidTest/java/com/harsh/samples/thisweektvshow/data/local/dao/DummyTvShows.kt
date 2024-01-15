package com.harsh.samples.thisweektvshow.data.local.dao

import com.harsh.samples.thisweektvshow.data.local.entity.TvShowEntity

object DummyTvShows {
    val gameOfThrones = TvShowEntity(
        id = 1,
        title = "Game of Thrones",
        overview = "Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.",
        posterUrl = "https://image.tmdb.org/t/p/w500/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/w1280/rIe3PnM6S7IBUmvNwDkBMX0i9EZ.jpg",
        voteAvg = 8.4f,
        isFavorite = true,
        isTrending = true,
        trendingNumber = 24
    )

    val houseOfTheDragon = TvShowEntity(
        id = 2,
        title = "House of the Dragon",
        overview = "The Targaryen dynasty is at the absolute apex of its power, with more than 15 dragons under their yoke. Most empires crumble from such heights. In the case of the Targaryens, their slow fall begins when King Viserys breaks with a century of tradition by naming his daughter Rhaenyra heir to the Iron Throne. But when Viserys later fathers a son, the court is shocked when Rhaenyra retains her status as his heir, and seeds of division sow friction across the realm.",
        posterUrl = "https://image.tmdb.org/t/p/w500/z2yahl2uefxDCl0nogcRBstwruJ.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/w1280/vXpeJJs1z8OKC88CNJX9O9QOhtr.jpg",
        voteAvg = 8.425f,
        isFavorite = false,
        isTrending = true,
        trendingNumber = 31
    )

    val moneyHeist = TvShowEntity(
        id = 3,
        title = "MoneyHeist",
        overview = "To carry out the biggest heist in history, a mysterious man called The Professor recruits a band of eight robbers who have a single characteristic: none of them has anything to lose. Five months of seclusion - memorizing every step, every detail, every probability - culminate in eleven days locked up in the National Coinage and Stamp Factory of Spain, surrounded by police forces and with dozens of hostages in their power, to find out whether their suicide wager will lead to everything or nothing.",
        posterUrl = "https://image.tmdb.org/t/p/w500/reEMJA1uzscCbkpeRJeTT2bjqUp.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/w1280/gFZriCkpJYsApPZEF3jhxL4yLzG.jpg",
        voteAvg = 7.531f,
        isFavorite = false,
        isTrending = false,
        trendingNumber = -1
    )
}
