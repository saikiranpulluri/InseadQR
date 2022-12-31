package com.konbini.inseadqr.data

class CustomerOrdersByStatusModel : ArrayList<CustomerOrdersByStatusModelItem>()

data class CustomerOrdersByStatusModelItem(
    val _links: Links,
    val barcode_url: String,
    val billing: Billing,
    val cart_hash: String,
    val cart_tax: String,
    val coupon_lines: List<Any>,
    val created_via: String,
    val currency: String,
    val currency_symbol: String,
    val customer_id: Int,
    val customer_ip_address: String,
    val customer_note: String,
    val customer_user_agent: String,
    val date_completed: Any,
    val date_completed_gmt: Any,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val date_paid: Any,
    val date_paid_gmt: Any,
    val discount_tax: String,
    val discount_total: String,
    val fee_lines: List<FeeLine>,
    val id: Int,
    val line_items: List<LineItem>,
    val meta_data: List<MetaDataX>,
    val number: String,
    val order_key: String,
    val parent_id: Int,
    val payment_method: String,
    val payment_method_title: String,
    val prices_include_tax: Boolean,
    val refunds: List<Any>,
    val shipping: Shipping,
    val shipping_lines: List<Any>,
    val shipping_tax: String,
    val shipping_total: String,
    val status: String,
    val tax_lines: List<TaxLine>,
    val total: String,
    val total_tax: String,
    val transaction_id: String,
    val version: String,
    var isExpanded: Boolean = false,
    var isSelected: Boolean = false
)

data class Links(
    val collection: List<Collection>,
    val customer: List<Customer>,
    val self: List<Self>
)

data class Billing(
    val address_1: String,
    val address_2: String,
    val city: String,
    val company: String,
    val country: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val postcode: String,
    val state: String
)

data class FeeLine(
    val amount: String,
    val id: Int,
    val meta_data: List<MetaData>,
    val name: String,
    val tax_class: String,
    val tax_status: String,
    val taxes: List<Any>,
    val total: String,
    val total_tax: String
)

data class LineItem(
    val id: Int,
    val meta_data: List<Any>,
    val name: String,
    val parent_name: Any,
    val price: Double,
    val product_id: Int,
    val quantity: Int,
    val sku: String,
    val subtotal: String,
    val subtotal_tax: String,
    val tax_class: String,
    val taxes: List<Tax>,
    val total: String,
    val total_tax: String,
    val variation_id: Int
)

data class MetaDataX(
    val id: Int,
    val key: String,
    val value: Any
)

data class Shipping(
    val address_1: String,
    val address_2: String,
    val city: String,
    val company: String,
    val country: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val postcode: String,
    val state: String
)

data class TaxLine(
    val compound: Boolean,
    val id: Int,
    val label: String,
    val meta_data: List<Any>,
    val rate_code: String,
    val rate_id: Int,
    val rate_percent: Int,
    val shipping_tax_total: String,
    val tax_total: String
)

data class Collection(
    val href: String
)

data class Customer(
    val href: String
)

data class Self(
    val href: String
)

data class MetaData(
    val display_key: String,
    val display_value: String,
    val id: Int,
    val key: String,
    val value: String
)

data class Tax(
    val id: Int,
    val subtotal: String,
    val total: String
)