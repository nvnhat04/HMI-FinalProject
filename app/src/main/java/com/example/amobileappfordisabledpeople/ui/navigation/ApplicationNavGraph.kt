package com.example.amobileappfordisabledpeople.ui.navigation

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.amobileappfordisabledpeople.features.face_recognition.FaceNetModel
import com.example.amobileappfordisabledpeople.features.object_detection.YuvToRgbConverter
import com.example.amobileappfordisabledpeople.ui.views.CameraPermission
import com.example.amobileappfordisabledpeople.ui.views.DangerWarningScreen
import com.example.amobileappfordisabledpeople.ui.views.DetectionScreen
import com.example.amobileappfordisabledpeople.ui.views.ExploreScreen
import com.example.amobileappfordisabledpeople.ui.views.FaceRecognitionScreen
import com.example.amobileappfordisabledpeople.ui.views.IntroScreen
import com.example.amobileappfordisabledpeople.ui.views.MoodTrackingScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.tensorflow.lite.Interpreter
import java.util.concurrent.ExecutorService

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ApplicationNavHost(
    navController: NavHostController,
    cameraExecutor: ExecutorService,
    yuvToRgbConverter: YuvToRgbConverter,
    interpreter: Interpreter,
    labels: List<String>,
    textToSpeech: TextToSpeech,
    modifier: Modifier = Modifier
) {

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
    } else {
        CameraPermission(cameraPermissionState)
    }

    NavHost(
        navController = navController,
        startDestination = Intro.route,
        modifier = modifier
    ) {
        composable(route = DetectionDestination.route) {
            DetectionScreen(cameraExecutor = cameraExecutor,
                yuvToRgbConverter = yuvToRgbConverter,
                interpreter = interpreter,
                labels = labels,
                textToSpeech = textToSpeech,
                navigateToFaceRecognition = {navController.navigate(FaceRecognition.route)},
                navigateToIntro = {navController.navigate(Intro.route)}
            )
        }
        composable(route = ExploreDestination.route) {
            ExploreScreen(
                navigateToDangerWarning = {navController.navigate(DangerWarningDestination.route)},
                navigateToDetection = {navController.navigate(DetectionDestination.route)},
                navigateToSocializingMode = {navController.navigate(FaceRecognition.route)}
            )

        }
        composable(route = MoodTrackingDestination.route) {
            MoodTrackingScreen(cameraExecutor = cameraExecutor,
                navigateToFaceRecognition = {navController.navigate(FaceRecognition.route)},
                navigateToExploreMode = {navController.navigate(DetectionDestination.route)},
                textToSpeech = textToSpeech
            )
        }
        composable(route = FaceRecognition.route) {
            FaceRecognitionScreen(
                cameraExecutor = cameraExecutor,
//                navigateToMoodTracking = {navController.navigate(MoodTrackingDestination.route)},
                navigateToExploreMode = {navController.navigate(ExploreDestination.route)},
                navigateToDetection = {navController.navigate(DetectionDestination.route)},
                navigateToIntro = {navController.navigate(Intro.route)},
                textToSpeech = textToSpeech
            )

        }
        composable(route = Intro.route) {
            IntroScreen(
                navigateToDetection = {navController.navigate(DetectionDestination.route)},
                textToSpeech = textToSpeech,
                navigateToFaceRecognition = {navController.navigate(FaceRecognition.route)}
            )

        }
    }
}