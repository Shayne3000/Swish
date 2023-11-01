package com.senijoshua.swish.util

import com.senijoshua.swish.data.Teams

val fakeTeamData = List(10) { index ->
    Teams(
        "Team $index",
        "Team $index logo"
    )
}
