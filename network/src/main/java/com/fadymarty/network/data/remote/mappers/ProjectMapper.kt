package com.fadymarty.network.data.remote.mappers

import com.fadymarty.network.data.remote.dto.ProjectDto
import com.fadymarty.network.domain.model.Project

fun ProjectDto.toProject(): Project {
    return Project(
        id = id,
        collectionId = collectionId,
        collectionName = collectionName,
        created = created,
        updated = updated,
        title = title,
        dateStart = dateStart,
        dateEnd = dateEnd,
        gender = gender,
        descriptionSource = descriptionSource,
        category = category,
        image = image,
        userId = userId,
        typeProject = typeProject
    )
}

fun Project.toProjectDto(): ProjectDto {
    return ProjectDto(
        id = id,
        collectionId = collectionId,
        collectionName = collectionName,
        created = created,
        updated = updated,
        title = title,
        dateStart = dateStart,
        dateEnd = dateEnd,
        gender = gender,
        descriptionSource = descriptionSource,
        category = category,
        image = image,
        userId = userId,
        typeProject = typeProject
    )
}