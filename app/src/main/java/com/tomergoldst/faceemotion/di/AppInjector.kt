package com.tomergoldst.faceemotion.di

import com.tomergoldst.faceemotion.BuildConfig
import com.tomergoldst.faceemotion.api.service.FaceDetectionService
import com.tomergoldst.faceemotion.domain.FaceService
import com.tomergoldst.faceemotion.domain.FaceServiceImpl
import com.tomergoldst.faceemotion.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val appModules: Module = module {

    factory { FaceDetectionService.create() }

    factory<FaceService> { FaceServiceImpl(BuildConfig.FACE_API_KEY, get()) }

    viewModel {  MainViewModel(get()) }

}

