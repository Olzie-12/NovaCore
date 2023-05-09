package net.zeeraa.novacore.spigot.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;

/**
 * A special version of the {@link ItemBuilder} that makes written books
 * <p>
 * You should always use {@link BookBuilder#setPages(List)},
 * {@link BookBuilder#addPages(List)},{@link BookBuilder#addPage(String)}
 * {@link BookBuilder#setTitle(String)} and
 * {@link BookBuilder#setAuthor(String)} before using functions from
 * {@link ItemBuilder} since they return {@link ItemBuilder} instead of
 * {@link BookBuilder}
 * <p>
 * Function documentation was taken from {@link BookMeta}
 * 
 * @author Zeeraa
 */
public class BookBuilder extends ItemBuilder {
	public BookBuilder() {
		this(null, null, null);
	}

	public BookBuilder(@Nonnull String title, @Nonnull String author) {
		this(null, title, author);
	}

	public BookBuilder(@Nonnull List<String> pages, @Nonnull String title, @Nonnull String author) {
		super(Material.WRITTEN_BOOK);

		if (pages != null) {
			setPages(pages);
		} else {
			((BookMeta) meta).setPages();
		}

		if (title != null) {
			setTitle(title);
		}

		if (author != null) {
			setAuthor(author);
		}
	}

	/**
	 * Clears the existing book pages, and sets the book to use the provided pages.
	 * Maximum 50 pages with 256 characters per page.
	 * <p>
	 * If used this should be called before any functions from {@link ItemBuilder}
	 *
	 * @param pages A list of pages to set the book to use
	 * @return Instance of the {@link BookBuilder}
	 */
	public BookBuilder setPages(@Nonnull List<String> pages) {
		((BookMeta) meta).setPages(pages);
		return this;
	}

	/**
	 * Adds new pages to the end of the book. Up to a maximum of 50 pages with 256
	 * characters per page.
	 * <p>
	 * If used this should be called before any functions from {@link ItemBuilder}
	 *
	 * @param pages A list of strings, each being a page
	 * @return Instance of the {@link BookBuilder}
	 */
	public BookBuilder addPages(@Nonnull String... pages) {
		for (String page : pages) {
			addPage(page);
		}

		return this;
	}

	/**
	 * Adds new pages to the end of the book. Up to a maximum of 50 pages with 256
	 * characters per page.
	 * <p>
	 * If used this should be called before any functions from {@link ItemBuilder}
	 *
	 * @param pages A list of strings, each being a page
	 * @return Instance of the {@link BookBuilder}
	 */
	public BookBuilder addPages(@Nonnull List<String> pages) {
		List<String> bookPages = getPageArrayListClone();

		bookPages.addAll(pages);

		((BookMeta) meta).setPages(bookPages);

		return this;
	}

	/**
	 * Adds a single page to the end of the book. Up to a maximum of 50 pages with
	 * 256 characters per page.
	 * <p>
	 * If used this should be called before any functions from {@link ItemBuilder}
	 *
	 * @param page A string containing the page text
	 * @return Instance of the {@link BookBuilder}
	 */
	public BookBuilder addPage(@Nonnull String page) {
		List<String> bookPages = getPageArrayListClone();

		bookPages.add(page);

		((BookMeta) meta).setPages(bookPages);

		return this;
	}

	/**
	 * Get a clone of the page list
	 * 
	 * @return {@link ArrayList} with pages
	 */
	private ArrayList<String> getPageArrayListClone() {
		return new ArrayList<String>(((BookMeta) meta).getPages());
	}

	/**
	 * Sets the title of the book.
	 * <p>
	 * Limited to 16 characters. Removes title when given null.
	 * <p>
	 * If used this should be called before any functions from {@link ItemBuilder}
	 *
	 * @param title the title to set
	 * @return Instance of the {@link BookBuilder}
	 */
	public BookBuilder setTitle(@Nonnull String title) {
		((BookMeta) meta).setTitle(title);
		return this;
	}

	/**
	 * Sets the author of the book. Removes author when given null.
	 * <p>
	 * If used this should be called before any functions from {@link ItemBuilder}
	 *
	 * @param author the author of the book
	 * @return Instance of the {@link BookBuilder}
	 */
	public BookBuilder setAuthor(@Nonnull String author) {
		((BookMeta) meta).setAuthor(author);
		return this;
	}

	/**
	 * Convert the {@link BookBuilder} to an {@link ItemBuilder}
	 * 
	 * @return {@link ItemBuilder} version of this instance
	 */
	public ItemBuilder toItemBuilder() {
		return this;
	}
}