package com.example.cameraapp.di

import com.example.cameraapp.feature.camera.CameraViewModel
import com.example.cameraapp.data.local.datasource.FileDataSource
import com.example.cameraapp.data.local.datasource.UserDataSource
import com.example.cameraapp.data.local.UserStore
import com.example.cameraapp.data.local.UserStoreImpl
import com.example.cameraapp.data.mapper.UserMapper
import com.example.cameraapp.feature.configuration.ConfigurationViewModel
import com.example.cameraapp.feature.gallery.GalleryViewModel
import com.example.cameraapp.feature.preview.PreviewViewModel
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    val localStores = module {
        factory<UserStore> {
            UserStoreImpl(get(), Json)
        }
    }

    val mappers = module {
        factory { UserMapper() }
    }

    val dataSources = module {
        factory { FileDataSource() }
        factory { UserDataSource(get(), get()) }
    }

    val viewModels = module {
        viewModel { CameraViewModel(get(), get()) }
        viewModel { GalleryViewModel(get()) }
        viewModel { ConfigurationViewModel(get()) }
        viewModel { PreviewViewModel(get()) }
    }
}