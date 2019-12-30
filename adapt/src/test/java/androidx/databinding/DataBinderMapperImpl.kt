package androidx.databinding

class DataBinderMapperImpl : MergedDataBinderMapper() {
    init {
        addMapper(me.emmano.adapt.DataBinderMapperImpl())
    }
}