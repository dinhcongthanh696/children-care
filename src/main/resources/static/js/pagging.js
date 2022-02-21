function renderPages() {
		var totalPages = parseInt(document.querySelector("#totalPages").value);
		var currentPage = parseInt(document.querySelector("#currentPage").value);
		var pageListContainer = document.querySelector("#pages");
		var gap = 2;
		
		var prevButton = "";
		if(currentPage == 0 && totalPages > 1){
			prevButton = "<span class='fas fa-arrow-left pageNumberElement' onclick=changePage(\'"+ (totalPages - 1) +"\')> </span>"
		}else if(totalPages > 1){
			prevButton = "<span class='fas fa-arrow-left pageNumberElement' onclick=changePage(\'"+ (currentPage - 1) +"\') > </span>";
		}

		pageListContainer.innerHTML += prevButton;
		
		if (currentPage > gap + 1) {
			pageListContainer.innerHTML += "<span class='pageNumberElement' onclick=changePage(\'0\')> 1 </span>"
					+ " <span class='pageNumberElement'> ... </span>"
		} else if (currentPage === gap + 1) {
			pageListContainer.innerHTML += "<span class='pageNumberElement' onclick=changePage(\'0\')> 1 </span>"
		}

		for (let i = 0; i < currentPage; i++) {
			if (i + gap >= currentPage) {
				pageListContainer.innerHTML += "<span class='pageNumberElement' onclick=changePage(\'"
						+ i + "\')> " + (i + 1) + "</span>"
			}
		}
		// Highlight currentpage
		pageListContainer.innerHTML += "<span class='pageNumberElement active' > "
				+ (currentPage + 1) + "</span>"
		//

		for (let i = currentPage + 1; i < totalPages - 1; i++) {
			if (i <= currentPage + gap) {
				pageListContainer.innerHTML += "<span class='pageNumberElement' onclick=changePage(\'"
						+ i + "\')> " + (i + 1) + "</span>"
			}
		}

		if (currentPage + gap + 1 < totalPages - 1) {
			pageListContainer.innerHTML += " <span class='pageNumberElement'> ... </span>"
					+ "<span class='pageNumberElement' onclick=changePage(\'"
					+ (totalPages - 1) + "\')> " + totalPages + "  </span>"
		} else if (currentPage !== totalPages - 1 && totalPages !== 0) {
			pageListContainer.innerHTML += "<span class='pageNumberElement' onclick=changePage(\'"
					+ (totalPages - 1) + "\')> " + totalPages + "  </span>"
		}
		
		var nextButton = "";

		if(currentPage == (totalPages - 1) && totalPages > 1){
			nextButton = "<span class='fas fa-arrow-right pageNumberElement' onclick=changePage('0')> </span>"
		}else if(totalPages > 1){
			nextButton = "<span class='fas fa-arrow-right pageNumberElement' onclick=changePage(\'"+ (currentPage + 1) +"\') > </span>";
		}

		pageListContainer.innerHTML += nextButton;
	}