package com.dukendev.genericgallery.presentation.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val DEFAULT_TRANSITION_DURATION = 1000

fun NavGraphBuilder.composableWithFadeTransition(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            fadeIn(tween(1500))
        },
        exitTransition = {
            fadeOut(tween(1500))
        },
        popEnterTransition = {
            fadeIn(tween(1000))
        },
        popExitTransition = {
            fadeOut(tween(1000))
        }
    ) {
        this.content(it)
    }
}

fun NavGraphBuilder.composableWithDefaultTransition(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = DEFAULT_TRANSITION_DURATION,
                    easing = FastOutLinearInEasing,
                    delayMillis = DEFAULT_TRANSITION_DURATION
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = DEFAULT_TRANSITION_DURATION,
                    easing = FastOutLinearInEasing,
                    delayMillis = DEFAULT_TRANSITION_DURATION
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = DEFAULT_TRANSITION_DURATION,
                    easing = FastOutLinearInEasing,
                    delayMillis = DEFAULT_TRANSITION_DURATION
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = DEFAULT_TRANSITION_DURATION,
                    easing = FastOutLinearInEasing,
                    delayMillis = DEFAULT_TRANSITION_DURATION
                )
            )
        },
    ) {
        this.content(it)
    }
}
