package fr.xgouchet.elmyr

interface ForgeryFactory<out T : Any> {

    fun getForgery(forge: Forge): T

}
