package se.axelkarlsson.hydroxide.util

import android.os.Process
import android.os.UserHandle

fun currentUserHandle(): UserHandle = Process.myUserHandle()