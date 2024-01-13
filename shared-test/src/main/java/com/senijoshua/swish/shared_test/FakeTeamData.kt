package com.senijoshua.swish.shared_test

import com.senijoshua.swish.data.Team
import com.senijoshua.swish.data.Teams

// Ideally Teams and Result will be in a data module on which shared-test will depend.
val fakeTeamsData = List(10) { index ->
    Teams(
        index,
        "Team $index name",
        false,
        "https://unsplash.com/photos/man-in-black-crew-neck-t-shirt-using-black-laptop-computer-b9-odQi5oDo?utm_content=creditShareLink&utm_medium=referral&utm_source=unsplash"
    )
}

val fakeTeamData = List(1) { index ->
    Team(
        index,
        "Team $index name",
        false,
        "https://unsplash.com/photos/man-in-black-crew-neck-t-shirt-using-black-laptop-computer-b9-odQi5oDo?utm_content=creditShareLink&utm_medium=referral&utm_source=unsplash"
    )
}
