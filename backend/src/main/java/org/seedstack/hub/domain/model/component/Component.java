/**
 * Copyright (c) 2015-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.hub.domain.model.component;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.seedstack.business.domain.BaseAggregateRoot;
import org.seedstack.hub.domain.model.document.DocumentId;
import org.seedstack.hub.domain.model.user.UserId;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(value = "components")
public class Component extends BaseAggregateRoot<ComponentId> {
    @Id
    @NotNull
    private ComponentId componentId;
    @NotNull
    private UserId owner;
    @NotNull
    private Description description;
    @NotNull
    private State state = State.PENDING;
    @NotNull
    private List<Comment> comments = new ArrayList<>();
    @NotNull
    private List<Version> versions = new ArrayList<>();
    @NotNull
    private List<DocumentId> docs = new ArrayList<>();
    @Min(0)
    private int stars = 0;
    private List<UserId> maintainers = new ArrayList<>();


    public Component(ComponentId componentId, UserId owner, Description description) {
        this.componentId = componentId;
        this.owner = owner;
        this.description = description;
    }

    private Component() {
        // required by morphia
    }

    @Override
    public ComponentId getEntityId() {
        return componentId;
    }

    public ComponentId getId() {
        return componentId;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void publish() throws ComponentException {
        if (state == State.PENDING || state == State.ARCHIVED) {
            state = State.PUBLISHED;
        } else {
            throw new ComponentException("Component cannot be published");
        }
    }

    public void archive() throws ComponentException {
        if (state == State.PUBLISHED) {
            state = State.ARCHIVED;
        } else {
            throw new ComponentException("Component cannot be archived");
        }
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public List<Version> getVersions() {
        return Collections.unmodifiableList(versions);
    }

    public void addVersion(Version version) {
        versions.add(version);
        Collections.sort(versions);
    }

    public void addDoc(DocumentId documentId) {
        docs.add(documentId);
    }

    public void replaceDocs(List<DocumentId> documentIds) {
        docs = new ArrayList<>(documentIds);
    }

    public List<DocumentId> getDocs() {
        return Collections.unmodifiableList(docs);
    }

    public UserId getOwner() {
        return owner;
    }

    public void star() {
        stars++;
    }

    public void unstar() {
        if (stars > 0) {
            stars--;
        }
    }

    public int getStars() {
        return stars;
    }

    public List<UserId> getMaintainers() {
        return Collections.unmodifiableList(maintainers);
    }

    public void addMaintainer(UserId maintainer) {
        this.maintainers.add(maintainer);
    }

    public void removeMaintainer(UserId maintainer) {
        this.maintainers.remove(maintainer);
    }

    public void replaceMaintainers(List<UserId> maintainers) {
        this.maintainers.clear();
        this.maintainers.addAll(maintainers);
    }
}
