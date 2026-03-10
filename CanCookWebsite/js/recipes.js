// Recipe Service
class RecipeService {
    
    // Get all recipes
    static async getAllRecipes() {
        try {
            return await ApiService.get(API_CONFIG.ENDPOINTS.RECIPES);
        } catch (error) {
            console.error('Failed to fetch recipes:', error);
            return [];
        }
    }
    
    // Get user's recipes
    static async getUserRecipes() {
        try {
            return await ApiService.get(API_CONFIG.ENDPOINTS.USER_RECIPES);
        } catch (error) {
            console.error('Failed to fetch user recipes:', error);
            return [];
        }
    }
    
    // Get recipe by ID
    static async getRecipeById(id) {
        try {
            return await ApiService.get(API_CONFIG.ENDPOINTS.RECIPE_BY_ID + id);
        } catch (error) {
            console.error('Failed to fetch recipe:', error);
            throw error;
        }
    }
    
    // Search recipes
    static async searchRecipes(query) {
        try {
            return await ApiService.get(API_CONFIG.ENDPOINTS.SEARCH_RECIPES, { q: query });
        } catch (error) {
            console.error('Failed to search recipes:', error);
            return [];
        }
    }
    
    // Add comment to recipe
    static async addComment(recipeId, rating, comment) {
        try {
            return await ApiService.post(API_CONFIG.ENDPOINTS.ADD_COMMENT, {
                recipeId,
                rating,
                comment
            });
        } catch (error) {
            console.error('Failed to add comment:', error);
            throw error;
        }
    }
}

// helper renders a recipe card, optionally showing a rating
function renderRecipeCard(recipe) {
    // stars string based on averageRating (rounded)
    const avg = recipe.averageRating || 0;
    const stars = '★'.repeat(Math.round(avg)) + '☆'.repeat(5 - Math.round(avg));
    return `
        <div class="card" onclick="showRecipeDetail(${recipe.id})">
            <img src="${recipe.imageUrl || 'apple.jpg'}" alt="${recipe.name}">
            <div class="overlay-text">
                <a href="#popup">${recipe.name}</a>
                ${avg > 0 ? `<div class="card-rating"><span class="stars">${stars}</span><span class="rating-value">${avg.toFixed(1)}</span></div>` : ''}
            </div>
        </div>
    `;
}

// Load and display all recipes
async function loadAllRecipes() {
    const recipes = await RecipeService.getAllRecipes();
    const container = document.querySelector('.content .all-recipes-carousel');
    
    if (container && recipes.length > 0) {
        container.innerHTML = recipes.map(renderRecipeCard).join('');
        // initialize infinite carousel behavior after content is inserted
        setupInfiniteCarousels();
    }
}

// Load and display user's recipes
async function loadUserRecipes() {
    if (!AuthService.isAuthenticated()) {
        return;
    }
    
    const recipes = await RecipeService.getUserRecipes();
    // user recipes go to their own carousel
    const userRecipesContainer = document.querySelector('.content .user-recipes-carousel');
    if (userRecipesContainer && recipes.length > 0) {
        userRecipesContainer.innerHTML = recipes.map(renderRecipeCard).join('');
    }
}

// Show recipe detail in popup
async function showRecipeDetail(recipeId) {
    try {
        const recipe = await RecipeService.getRecipeById(recipeId);
        
        // Update popup content
        const popup = document.getElementById('popup');
        if (popup) {
            const recipePage = popup.querySelector('.recipe-page');
            
            recipePage.querySelector('.recipe-image img').src = recipe.imageUrl || 'apple.jpg';
            recipePage.querySelector('h1').textContent = recipe.name;
            recipePage.querySelector('.meta-info .value').textContent = recipe.cookTime;
            recipePage.querySelector('.rating-value').textContent = recipe.averageRating || '0.0';
            
            // Update stars
            const stars = '★'.repeat(Math.round(recipe.averageRating || 0)) + '☆'.repeat(5 - Math.round(recipe.averageRating || 0));
            recipePage.querySelector('.stars').textContent = stars;
            
            // Update ingredients
            const ingredientsList = recipePage.querySelector('.ingredients ul');
            ingredientsList.innerHTML = recipe.ingredients.map(ing => 
                `<li>${ing.name} - ${ing.quantity} ${ing.unit}</li>`
            ).join('');
            
            // Update instructions
            const instructionsList = recipePage.querySelector('.instructions ol');
            instructionsList.innerHTML = recipe.instructions.map(inst => 
                `<li>${inst}</li>`
            ).join('');
            
            // Update comments
            const commentsSection = popup.querySelector('.comments-section');
            const existingComments = recipe.comments || [];
            const commentsHTML = existingComments.map(comment => `
                <div class="comment">
                    <div class="comment-header">
                        <strong>${comment.username}</strong>
                        <span class="stars">${'★'.repeat(comment.rating)}${'☆'.repeat(5 - comment.rating)}</span>
                    </div>
                    <p>${comment.text}</p>
                </div>
            `).join('');
            
            // Find or create comments container
            let commentsContainer = commentsSection.querySelector('.comments-list');
            if (!commentsContainer) {
                commentsContainer = document.createElement('div');
                commentsContainer.className = 'comments-list';
                commentsSection.appendChild(commentsContainer);
            }
            commentsContainer.innerHTML = commentsHTML;
        }
    } catch (error) {
        console.error('Failed to load recipe details:', error);
        alert('Failed to load recipe details');
    }
}

// Handle search form
async function handleSearch(event) {
    event.preventDefault();
    const searchQuery = document.getElementById('site-search').value;
    
    if (searchQuery.trim()) {
        const recipes = await RecipeService.searchRecipes(searchQuery);
        const container = document.querySelector('.content .all-recipes-carousel');
        
        if (container) {
            if (recipes.length > 0) {
                container.innerHTML = recipes.map(renderRecipeCard).join('');
                // re-init infinite carousel after search results update
                setupInfiniteCarousels();
            } else {
                container.innerHTML = '<p>No recipes found</p>';
            }
        }
    }
}

// Initialize recipes page
document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname.includes('index.html') || window.location.pathname === '/') {
        loadAllRecipes();
        loadUserRecipes();
        
        // Attach search handler
        const searchForm = document.querySelector('search form');
        if (searchForm) {
            searchForm.addEventListener('submit', handleSearch);
        }
    }
});

// --- Infinite carousel helpers ---
function setupInfiniteCarousels() {
    const carousels = document.querySelectorAll('.content .all-recipes-carousel');
    carousels.forEach(container => {
        try {
            makeCarouselInfinite(container);
        } catch (e) {
            console.error('Failed to make carousel infinite', e);
        }
    });
}

function makeCarouselInfinite(container) {
    if (!container || container.dataset.infinite === 'true') return;

    const gap = parseFloat(getComputedStyle(container).gap) || 0;
    const originals = Array.from(container.querySelectorAll(':scope > .card'));
    if (originals.length === 0) return;

    // remove previously added clones if any
    Array.from(container.querySelectorAll(':scope > [data-clone]')).forEach(n => n.remove());

    // clone nodes
    const clonesBefore = originals.map(n => { const c = n.cloneNode(true); c.setAttribute('data-clone','before'); return c; });
    const clonesAfter = originals.map(n => { const c = n.cloneNode(true); c.setAttribute('data-clone','after'); return c; });

    // prepend and append
    container.prepend(...clonesBefore);
    container.append(...clonesAfter);

    const measureTotalWidth = (nodes) => {
        if (!nodes.length) return 0;
        let sum = 0;
        nodes.forEach((n, i) => {
            const w = n.getBoundingClientRect().width;
            sum += w;
            if (i < nodes.length - 1) sum += gap;
        });
        return sum;
    };

    const beforeWidth = measureTotalWidth(clonesBefore);
    const originalWidth = measureTotalWidth(originals);

    // initial scroll to the start of originals
    container.scrollLeft = beforeWidth;

    // adjust on scroll into clones
    let isAdjusting = false;
    const threshold = 2;
    container.addEventListener('scroll', () => {
        if (isAdjusting) return;
        const sl = container.scrollLeft;
        // entered left-clones
        if (sl <= beforeWidth - threshold) {
            isAdjusting = true;
            container.scrollLeft = beforeWidth + originalWidth - (beforeWidth - sl);
            isAdjusting = false;
        }
        // entered right-clones
        else if (sl >= beforeWidth + originalWidth - threshold) {
            isAdjusting = true;
            container.scrollLeft = beforeWidth + (sl - (beforeWidth + originalWidth));
            isAdjusting = false;
        }
    });

    container.dataset.infinite = 'true';
}

// try to setup once on load (in case carousels exist statically)
window.addEventListener('load', () => setTimeout(setupInfiniteCarousels, 150));
