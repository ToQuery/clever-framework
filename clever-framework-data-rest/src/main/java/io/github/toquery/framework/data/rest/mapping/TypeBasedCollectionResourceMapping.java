package io.github.toquery.framework.data.rest.mapping;

import io.github.toquery.framework.data.rest.annotation.AppEntityRest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.rest.core.Path;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.mapping.AnnotationBasedResourceDescription;
import org.springframework.data.rest.core.mapping.CollectionResourceMapping;
import org.springframework.data.rest.core.mapping.ResourceDescription;
import org.springframework.data.rest.core.mapping.SimpleResourceDescription;
import org.springframework.data.util.Lazy;
import org.springframework.data.util.Optionals;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Modifier;
import java.util.Optional;

/**
 * {@link CollectionResourceMapping} based on a type. Will derive default relation types and pathes from the type but
 * inspect it for {@link AppEntityRest} annotations for customization.
 *
 * @author Oliver Gierke
 */
class TypeBasedCollectionResourceMapping implements CollectionResourceMapping {

	private final Class<?> type;
	private final LinkRelationProvider relProvider;
	private final Optional<AppEntityRest> annotation;

	private final Lazy<Path> path;
	private final Lazy<LinkRelation> rel;
	private final Lazy<ResourceDescription> description, itemResourceDescription;

	/**
	 * Creates a new {@link TypeBasedCollectionResourceMapping} using the given type.
	 *
	 * @param type must not be {@literal null}.
	 */
	public TypeBasedCollectionResourceMapping(Class<?> type) {
		this(type, new EvoInflectorLinkRelationProvider());
	}

	/**
	 * Creates a new {@link TypeBasedCollectionResourceMapping} using the given type and {@link RelProvider}.
	 *
	 * @param type must not be {@literal null}.
	 * @param relProvider must not be {@literal null}.
	 */
	public TypeBasedCollectionResourceMapping(Class<?> type, LinkRelationProvider relProvider) {

		Assert.notNull(type, "Type must not be null!");
		Assert.notNull(relProvider, "LinkRelationProvider must not be null!");

		this.type = type;
		this.relProvider = relProvider;
		this.annotation = Optional.ofNullable(AnnotationUtils.findAnnotation(type, AppEntityRest.class));

		this.path = Lazy.of(() -> annotation.map(AppEntityRest::path) //
				.map(String::trim) //
				.filter(StringUtils::hasText) //
				.orElseGet(() -> getDefaultPathFor(type)))//
				.map(Path::new);

		this.rel = Lazy.of(() -> annotation //
				.map(AppEntityRest::rel) //
				.filter(StringUtils::hasText) //
				.map(LinkRelation::of) //
				.orElseGet(() -> relProvider.getCollectionResourceRelFor(type)));

		Optional<Description> descriptionAnnotation = Optional
				.ofNullable(AnnotationUtils.findAnnotation(type, Description.class));

		this.description = Lazy.of(() -> {

			ResourceDescription fallback = SimpleResourceDescription.defaultFor(getRel());

			return Optionals.<ResourceDescription> firstNonEmpty(//
					() -> descriptionAnnotation.map(it -> new AnnotationBasedResourceDescription(it, fallback)), //
					() -> annotation.map(AppEntityRest::description)
							.map(it -> new AnnotationBasedResourceDescription(it, fallback))) //
					.orElse(fallback);
		});

		this.itemResourceDescription = Lazy.of(() -> {

			ResourceDescription fallback = SimpleResourceDescription.defaultFor(getItemResourceRel());

			return Optionals.<ResourceDescription> firstNonEmpty(//
					() -> annotation.map(AppEntityRest::description) //
							.filter(it -> StringUtils.hasText(it.value())) //
							.map(it -> new AnnotationBasedResourceDescription(it, fallback)), //
					() -> descriptionAnnotation.map(it -> new AnnotationBasedResourceDescription(it, fallback))) //
					.orElse(fallback);
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.mapping.ResourceMapping#getPath()
	 */
	@Override
	public Path getPath() {
		return path.get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.mapping.ResourceMapping#isExported()
	 */
	@Override
	public boolean isExported() {

		return annotation //
				.map(AppEntityRest::exported) //
				.orElseGet(() -> Modifier.isPublic(type.getModifiers()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.mapping.ResourceMapping#getRel()
	 */
	@Override
	public LinkRelation getRel() {
		return rel.get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.mapping.CollectionResourceMapping#getSingleResourceRel()
	 */
	@Override
	public LinkRelation getItemResourceRel() {
		return relProvider.getItemResourceRelFor(type);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.mapping.CollectionResourceMapping#isPagingResource()
	 */
	@Override
	public boolean isPagingResource() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.mapping.ResourceMapping#getDescription()
	 */
	@Override
	public ResourceDescription getDescription() {
		return description.get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.mapping.CollectionResourceMapping#getItemResourceDescription()
	 */
	@Override
	public ResourceDescription getItemResourceDescription() {
		return itemResourceDescription.get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.mapping.CollectionResourceMapping#getExcerptProjection()
	 */
	@Override
	public Optional<Class<?>> getExcerptProjection() {
		return Optional.empty();
	}

	/**
	 * Returns the default path to be used if the path is not configured manually.
	 *
	 * @param type must not be {@literal null}.
	 * @return
	 */
	protected String getDefaultPathFor(Class<?> type) {
		return getSimpleTypeName(type);
	}

	private String getSimpleTypeName(Class<?> type) {
		return StringUtils.uncapitalize(type.getSimpleName());
	}
}
