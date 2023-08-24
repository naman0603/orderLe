package com.example.orderleapp.viewModel

import com.example.orderleapp.apiResponse.ProductApiResponse

class DataHolder {
    companion object {
        private var productApiResponse: ProductApiResponse? = null

        fun setProductApiResponse(data: ProductApiResponse) {
            productApiResponse = data
        }

        fun getProductApiResponse(): ProductApiResponse? {
            return productApiResponse
        }
    }
}

