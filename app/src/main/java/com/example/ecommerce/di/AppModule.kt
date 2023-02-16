package com.example.ecommerce.di

import com.example.ecommerce.data.interfaces.*
import com.example.ecommerce.data.repositories.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
public object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesFirestoreRef(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesAuthRepository(impl: AuthRepo): AuthRepoInterface = impl

    @Provides
    fun providesProductsRepository(impl: ProductsRepo): ProductsRepoInterface = impl

    @Provides
    fun providesCartRepository(impl: CartRepo): CartRepoInterface = impl

    @Provides
    fun providesOrdersRepository(impl: OrdersRepo): OrdersRepoInterface = impl

    @Provides
    fun providesProfileRepository(impl: ProfileRepo): ProfileRepoInterface = impl


}