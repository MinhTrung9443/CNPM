
let mainCheckbox = document.querySelector(".select-all-products");

mainCheckbox.onclick = function () {
    let subCheckbox = document.querySelectorAll(".select-product");
    if (mainCheckbox.checked)
        for (let checkbox of subCheckbox)
            checkbox.setAttribute("checked", "checked")
    else if (!mainCheckbox.checked)
        for (let checkbox of subCheckbox)
            checkbox.removeAttribute("checked");
}

let buttonNumbers = document.querySelectorAll(".quantity-buttons--number");
let buttonMinus = document.querySelectorAll(".quantity-buttons--minus");
let buttonPlus = document.querySelectorAll(".quantity-buttons--plus");

for(let i = 0; i < buttonNumbers.length; i++) {
    buttonMinus[i].onclick = function () {
        if(buttonNumbers[i].innerText > 0)
            buttonNumbers[i].innerText -= 1;
        else {
            confirm("Do you want to remove this item from cart?")
            cartItems[i].style.display = "none";
        }

        buttonNumbers[i].innerText > 0 ? buttonNumbers[i].innerText -= 1 : buttonNumbers[i] = 0;
    }
    buttonPlus[i].onclick = function () {
        buttonNumbers[i].innerText = Number(buttonNumbers[i].innerHTML) + 1;
    }
}

let deleteButtons = document.querySelectorAll(".filled-cart__item-delete-button .buttons");
let cartItems = document.querySelectorAll(".filled-cart__item");

for(let i = 0; i < deleteButtons.length; i++)
    deleteButtons[i].onclick = function () {
        cartItems[i].style.display = "none";
    }
