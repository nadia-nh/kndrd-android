package com.kndrd.android.feature.feed

import com.kndrd.android.core.model.ChatRoom
import com.kndrd.android.core.model.Interest
import com.kndrd.android.core.model.Plan
import com.kndrd.android.feature.chats.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepositoryImpl @Inject constructor(
    private val chatRepository: ChatRepository,
) : FeedRepository {

    private val _plans = MutableStateFlow(seedPlans())

    override fun observePlans(): Flow<List<Plan>> = _plans

    override fun observePlansByInterest(interest: Interest): Flow<List<Plan>> =
        _plans.map { plans -> plans.filter { it.interest == interest } }

    override suspend fun getPlan(id: String): Plan? = _plans.value.find { it.id == id }

    override suspend fun joinPlan(planId: String, userId: String): Result<ChatRoom> {
        delay(400)
        val plan = _plans.value.find { it.id == planId }
            ?: return Result.failure(IllegalArgumentException("Plan not found"))

        val updatedPlan = plan.copy(
            isJoined = true,
            currentAttendees = plan.currentAttendees + 1,
            attendeeIds = plan.attendeeIds + userId,
        )
        _plans.value = _plans.value.map { if (it.id == planId) updatedPlan else it }

        val room = ChatRoom(
            id = plan.chatRoomId,
            planId = plan.id,
            planTitle = plan.title,
            memberIds = updatedPlan.attendeeIds,
            lastMessage = null,
            lastMessageTimeMs = null,
            unreadCount = 0,
        )
        chatRepository.addRoom(room)
        return Result.success(room)
    }

    override suspend fun createPlan(plan: Plan): Result<Plan> {
        delay(400)
        val newPlan = plan.copy(
            id = UUID.randomUUID().toString(),
            chatRoomId = UUID.randomUUID().toString(),
            isJoined = true,
            currentAttendees = 1,
        )
        _plans.value = _plans.value + newPlan
        val room = ChatRoom(
            id = newPlan.chatRoomId,
            planId = newPlan.id,
            planTitle = newPlan.title,
            memberIds = listOf(newPlan.hostId),
            lastMessage = null,
            lastMessageTimeMs = null,
            unreadCount = 0,
        )
        chatRepository.addRoom(room)
        return Result.success(newPlan)
    }

    override suspend fun refresh() {
        delay(300)
    }
}

private val currentTimeMs = System.currentTimeMillis()
private val oneHour = 3_600_000L
private val oneDay = 86_400_000L

private fun seedPlans() = listOf(
    Plan(
        id = "plan-1",
        title = "Morning coffee at Blue Bottle",
        description = "Let's grab coffee and chat! I'm new to Williamsburg and would love to meet people. All levels of caffeine addiction welcome ☕",
        hostId = "user-2",
        hostName = "Maya K.",
        hostPhotoUrl = null,
        interest = Interest.COFFEE,
        location = "Blue Bottle Coffee, Williamsburg, Brooklyn",
        dateTimeMs = currentTimeMs + 2 * oneHour,
        maxAttendees = 6,
        currentAttendees = 3,
        attendeeIds = listOf("user-2", "user-3", "user-4"),
        imageUrl = null,
        chatRoomId = "chat-1",
        isJoined = false,
    ),
    Plan(
        id = "plan-2",
        title = "Sunrise yoga in Central Park",
        description = "Starting the week right with a gentle flow at the Great Lawn. Bring a mat, I'll bring the good vibes 🧘",
        hostId = "user-5",
        hostName = "Priya S.",
        hostPhotoUrl = null,
        interest = Interest.FITNESS,
        location = "Great Lawn, Central Park, Manhattan",
        dateTimeMs = currentTimeMs + oneDay,
        maxAttendees = 10,
        currentAttendees = 5,
        attendeeIds = listOf("user-5", "user-6", "user-7", "user-8", "user-9"),
        imageUrl = null,
        chatRoomId = "chat-2",
        isJoined = false,
    ),
    Plan(
        id = "plan-3",
        title = "Gallery opening in Chelsea",
        description = "New show at Hauser & Wirth — abstract sculpture and mixed media. Wine and conversation guaranteed 🎨",
        hostId = "user-10",
        hostName = "Zoe M.",
        hostPhotoUrl = null,
        interest = Interest.ART,
        location = "Hauser & Wirth, Chelsea, Manhattan",
        dateTimeMs = currentTimeMs + 3 * oneHour,
        maxAttendees = 8,
        currentAttendees = 4,
        attendeeIds = listOf("user-10", "user-11", "user-12", "user-13"),
        imageUrl = null,
        chatRoomId = "chat-3",
        isJoined = false,
    ),
    Plan(
        id = "plan-4",
        title = "Rooftop drinks in LIC",
        description = "Catching the Manhattan skyline at golden hour with good company. BYOB friendly, views are on the house 🌙",
        hostId = "user-14",
        hostName = "Jasmine T.",
        hostPhotoUrl = null,
        interest = Interest.NIGHTLIFE,
        location = "LIC Landing, Long Island City, Queens",
        dateTimeMs = currentTimeMs + 5 * oneHour,
        maxAttendees = 12,
        currentAttendees = 7,
        attendeeIds = listOf("user-14", "user-15", "user-16", "user-17", "user-18", "user-19", "user-20"),
        imageUrl = null,
        chatRoomId = "chat-4",
        isJoined = false,
    ),
    Plan(
        id = "plan-5",
        title = "Park Slope book club",
        description = "We're reading 'Tomorrow, and Tomorrow, and Tomorrow'. First timers welcome, no need to finish the book to join the chat 📚",
        hostId = "user-21",
        hostName = "Nadia H.",
        hostPhotoUrl = null,
        interest = Interest.BOOKS,
        location = "Community Bookstore, Park Slope, Brooklyn",
        dateTimeMs = currentTimeMs + 2 * oneDay,
        maxAttendees = 8,
        currentAttendees = 6,
        attendeeIds = listOf("user-21", "user-22", "user-23", "user-24", "user-25", "user-26"),
        imageUrl = null,
        chatRoomId = "chat-5",
        isJoined = false,
    ),
    Plan(
        id = "plan-6",
        title = "Hike at Inwood Hill Park",
        description = "Exploring the old-growth forest in the northern tip of Manhattan. Wear sneakers, bring water 🌿",
        hostId = "user-27",
        hostName = "Chloe R.",
        hostPhotoUrl = null,
        interest = Interest.OUTDOORS,
        location = "Inwood Hill Park, Inwood, Manhattan",
        dateTimeMs = currentTimeMs + 2 * oneDay + 4 * oneHour,
        maxAttendees = 10,
        currentAttendees = 4,
        attendeeIds = listOf("user-27", "user-28", "user-29", "user-30"),
        imageUrl = null,
        chatRoomId = "chat-6",
        isJoined = false,
    ),
    Plan(
        id = "plan-7",
        title = "Ramen crawl in the East Village",
        description = "Hitting 3 spots in one night — Momofuku, Ivan Ramen, and Totto. Come hungry 🍜",
        hostId = "user-31",
        hostName = "Yuki A.",
        hostPhotoUrl = null,
        interest = Interest.FOOD,
        location = "East Village, Manhattan",
        dateTimeMs = currentTimeMs + 6 * oneHour,
        maxAttendees = 6,
        currentAttendees = 2,
        attendeeIds = listOf("user-31", "user-32"),
        imageUrl = null,
        chatRoomId = "chat-7",
        isJoined = false,
    ),
    Plan(
        id = "plan-8",
        title = "Live jazz at Smalls",
        description = "Late-night session at the best jazz club in the West Village. No cover after midnight 🎵",
        hostId = "user-33",
        hostName = "Amara D.",
        hostPhotoUrl = null,
        interest = Interest.MUSIC,
        location = "Smalls Jazz Club, West Village, Manhattan",
        dateTimeMs = currentTimeMs + 8 * oneHour,
        maxAttendees = 8,
        currentAttendees = 5,
        attendeeIds = listOf("user-33", "user-34", "user-35", "user-36", "user-37"),
        imageUrl = null,
        chatRoomId = "chat-8",
        isJoined = false,
    ),
)
