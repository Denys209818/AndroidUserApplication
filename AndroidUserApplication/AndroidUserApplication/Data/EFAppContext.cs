using AndroidUserApplication.Data.Identity.Configuration;
using AndroidUserApplication.Data.Identity.Entities;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace AndroidUserApplication.Data
{
    //Контекст для використання обєктів БД
    public class EFAppContext : IdentityDbContext<AppUser, AppRole, long, IdentityUserClaim<long>
        , AppUserRole, IdentityUserLogin<long>, IdentityRoleClaim<long>, IdentityUserToken<long>>
    {

        public EFAppContext(DbContextOptions opts) : base(opts)
        {

        }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            //Застосування налаштувань Identity
            #region Identity
            builder.ApplyConfiguration(new IdentityConfiguration());
            #endregion
        }
    }
}
