package com.senijoshua.swish.shared_test

import com.senijoshua.swish.data.Teams

// Ideally Teams and Result will be in a data module on which shared-test will depend.
val fakeTeamData = List(10) { index ->
    Teams(
        "Team $index",
        "Team $index logo"
    )
}
